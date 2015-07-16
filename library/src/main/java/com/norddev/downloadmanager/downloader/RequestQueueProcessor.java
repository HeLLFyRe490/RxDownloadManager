package com.norddev.downloadmanager.downloader;

import android.os.Handler;
import android.os.Message;

import com.norddev.downloadmanager.common.EventLoop;
import com.norddev.downloadmanager.queue.RequestQueue;

/**
 *
 */
public class RequestQueueProcessor implements Downloader.Listener, RequestQueue.Listener {
    private static final int FINISH_REQUEST = 0;
    private static final int PROCESS_NEXT_REQUEST = 1;
    private static final int INTERRUPT_IF_REQUEST = 2;
    private final EventLoop mEventLoop;
    private final Downloader mDownloader;
    private final RequestQueue mQueue;

    private final Handler.Callback mCallback = new Handler.Callback() {

        private DownloadRequest mCurrentRequest;

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESS_NEXT_REQUEST:
                    if (mCurrentRequest == null) {
                        DownloadRequest request = mQueue.peek();
                        if (request != null) {
                            mCurrentRequest = request;
                            mDownloader.execute(request);
                        }
                    }
                    break;
                case FINISH_REQUEST:
                    mCurrentRequest = null;
                    mQueue.remove((DownloadRequest) msg.obj);
                    break;
                case INTERRUPT_IF_REQUEST:
                    if (msg.obj.equals(mCurrentRequest)) {
                        mDownloader.interrupt();
                    }
                    break;
            }
            return false;
        }
    };

    public RequestQueueProcessor(Downloader downloader, RequestQueue queue) {
        mEventLoop = new EventLoop("RequestQueueProcessorEventLoop", mCallback);
        mDownloader = downloader;
        mQueue = queue;
    }

    public void start() {
        mQueue.addListener(this);
        mDownloader.addListener(this);
        mDownloader.start();
        mEventLoop.start();
        mEventLoop.post(PROCESS_NEXT_REQUEST);
    }

    public void shutdown() {
        mQueue.removeListener(this);
        mDownloader.removeListener(this);
        mDownloader.shutdown();
        mEventLoop.quit();
    }

    @Override
    public void onDownloaderStarted() {

    }

    @Override
    public void onDownloadComplete(DownloadRequest request) {
        mEventLoop.post(FINISH_REQUEST, request);
        mEventLoop.post(PROCESS_NEXT_REQUEST);
    }

    @Override
    public void onDownloadResponseReceived(DownloadRequest request, long contentLength) {
        
    }

    @Override
    public void onDownloadProgress(DownloadRequest request, long bytesDownloaded) {

    }

    @Override
    public void onDownloadError(DownloadRequest request, Exception error) {

    }

    @Override
    public void onDownloaderShutdown() {

    }

    @Override
    public void onRequestAdded(DownloadRequest request) {
        mEventLoop.post(PROCESS_NEXT_REQUEST);
    }

    @Override
    public void onDownloadInterrupted() {

    }

    @Override
    public void onRequestRemoved(DownloadRequest request) {
        mEventLoop.post(INTERRUPT_IF_REQUEST, request);
    }

    public boolean isRunning() {
        return mEventLoop.isRunning();
    }
}