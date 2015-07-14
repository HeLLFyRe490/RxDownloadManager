package com.norddev.downloadmanager.downloader;

import android.support.annotation.NonNull;
import android.util.Log;

import com.norddev.downloadmanager.C;
import com.norddev.downloadmanager.Util;
import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.norddev.downloadmanager.downloader.api.RetryPolicy;
import com.norddev.downloadmanager.queue.DownloadRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.Map;

import okio.BufferedSource;

/**
 *
 */
public class DownloadTask {

    private static final String TAG = "DownloadTask";
    private final Callback mCallback;
    private final DownloadRequest mDownloadRequest;
    private Call mCall;
    private boolean mInterrupted;

    /**
     * @param downloadRequest
     * @param callback
     */
    public DownloadTask(@NonNull DownloadRequest downloadRequest, @NonNull Callback callback) {
        mDownloadRequest = downloadRequest;
        mCallback = callback;
    }

    public DownloadRequest getDownloadRequest() {
        return mDownloadRequest;
    }

    /**
     *
     */
    public void execute() {
        synchronized (this) {
            mInterrupted = false;
        }
        DownloadHandler downloadHandler = mDownloadRequest.getDownloadHandler();
        RetryPolicy retryPolicy = mDownloadRequest.getRetryPolicy();

        long requestedLength;
        long requestedOffset;

        String rangeValue = mDownloadRequest.getRequestHeaders().get("Range");
        if (rangeValue != null) {
            long[] requestedRange = Util.parseRange(rangeValue);
            if (requestedRange != null) {
                requestedOffset = requestedRange[0];
                if (requestedRange[1] != C.UNKNOWN) {
                    requestedLength = requestedRange[1] - requestedRange[0] + 1;
                } else {
                    requestedLength = C.UNKNOWN;
                }
            } else {
                requestedOffset = 0;
                requestedLength = C.UNKNOWN;
            }
        } else {
            requestedOffset = 0;
            requestedLength = C.UNKNOWN;
        }

        do {
            try {
                downloadHandler.onRequestStarted(mDownloadRequest);

                long bytesDownloaded = downloadHandler.getPosition();
                long rangeOffset = requestedOffset + bytesDownloaded;

                Request.Builder builder = new Request.Builder();
                builder.url(mDownloadRequest.getURL());
                Map<String, String> requestHeaders = mDownloadRequest.getRequestHeaders();
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }
                //only make a range request if the current offset requires it or the user specified a range
                if (rangeOffset > 0 || rangeValue != null) {
                    String range = Util.buildRange(rangeOffset, requestedLength);
                    builder.header("Range", range);
                }
                builder.get();
                Request request = builder.build();

                OkHttpClient client = new OkHttpClient();

                synchronized (this) {
                    if (mInterrupted) {
                        throw new InterruptedException("Interrupted");
                    }
                    mCall = client.newCall(request);
                }
                Response response = mCall.execute();
                Log.d(TAG, response.toString());
                Log.d(TAG, response.headers().toString());

                if (requestedLength == C.UNKNOWN) {
                    long[] contentRange = Util.parseContentRange(response.header("Content-Range"));
                    if (contentRange != null) {
                        requestedLength = contentRange[2];
                    } else {
                        requestedLength = response.body().contentLength();
                    }
                }
                Log.d(TAG, "Response received: length = " + Util.formatFileSize(requestedLength));

                downloadHandler.onResponseReceived(response);
                mCallback.onResponse(this, requestedLength);

                //request must not be already completed
                if (requestedLength == C.UNKNOWN || bytesDownloaded < requestedLength) {
                    if (response.isSuccessful()) {
                        long totalBytesReceived = bytesDownloaded;
                        byte[] buffer = new byte[8192];
                        BufferedSource source = response.body().source();
                        for (; ; ) {
                            int bytesRead = source.read(buffer);
                            if (bytesRead != -1) {
                                totalBytesReceived += bytesRead;
                                downloadHandler.onBytesReceived(buffer, bytesRead);
                                mCallback.onProgress(this, totalBytesReceived);
                            } else {
                                downloadHandler.onRequestFinished();
                                break;
                            }
                        }
                    } else {
                        throw new HTTPResponseException(response);
                    }
                } else {
                    mCallback.onProgress(this, bytesDownloaded);
                }

                retryPolicy.onSuccess();
                mCallback.onComplete(this);

            } catch (Exception e) {
                if (mInterrupted) {
                    e = new InterruptedException("Interrupted");
                }
                e.printStackTrace();
                downloadHandler.onError(e);
                retryPolicy.onError(e);
                mCallback.onError(this, e);
            }

        } while (retryPolicy.shouldRetry());
    }

    public void interrupt() {
        synchronized (this) {
            mInterrupted = true;
            mCall.cancel();
        }
    }

    public interface Callback {
        void onComplete(DownloadTask task);

        void onResponse(DownloadTask task, long contentLength);

        void onProgress(DownloadTask task, long bytes);

        void onError(DownloadTask task, Exception e);
    }

}
