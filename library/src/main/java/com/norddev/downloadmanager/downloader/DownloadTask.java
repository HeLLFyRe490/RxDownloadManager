package com.norddev.downloadmanager.downloader;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.queue.DownloadRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.Map;

import okio.BufferedSource;

public class DownloadTask {

    private final Callback mCallback;
    private final DownloadRequest mDownloadRequest;

    public interface Callback {
        void onTaskComplete(DownloadTask task);
    }

    public DownloadRequest getDownloadRequest() {
        return mDownloadRequest;
    }

    /**
     * @param downloadRequest
     * @param callback
     */
    public DownloadTask(@NonNull DownloadRequest downloadRequest, @NonNull Callback callback) {
        mDownloadRequest = downloadRequest;
        mCallback = callback;
    }

    /**
     *
     */
    public void execute() {
        DownloadHandler downloadHandler = mDownloadRequest.getDownloadHandler();
        RetryPolicy retryPolicy = mDownloadRequest.getRetryPolicy();

        do {
            try {
                downloadHandler.onRequest(mDownloadRequest);

                long bytesDownloaded = downloadHandler.getPosition();
                long requestOffset = 0; //TODO
                long rangeOffset = requestOffset + bytesDownloaded;

                Request.Builder builder = new Request.Builder();
                builder.url(mDownloadRequest.getURL());
                Map<String, String> requestHeaders = mDownloadRequest.getRequestHeaders();
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }
                builder.header("Range", String.format("bytes=%d-", rangeOffset));
                builder.get();
                Request request = builder.build();

                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);
                Response response = call.execute();

                if (response.isSuccessful()) {

                    byte[] buffer = new byte[8192];
                    BufferedSource source = response.body().source();
                    for (; ; ) {
                        int bytesRead = source.read(buffer);
                        if (bytesRead != -1) {
                            downloadHandler.onBytesReceived(buffer, bytesRead);
                        } else {
                            downloadHandler.onResponseFinished();
                            break;
                        }
                    }
                } else {
                    throw new HTTPResponseException(response);
                }

                retryPolicy.onSuccess();
                mCallback.onTaskComplete(this);

            } catch (Exception e) {
                e.printStackTrace();
                downloadHandler.onError(e);
                retryPolicy.onError(e);
            }

        } while (retryPolicy.shouldRetry());
    }
}
