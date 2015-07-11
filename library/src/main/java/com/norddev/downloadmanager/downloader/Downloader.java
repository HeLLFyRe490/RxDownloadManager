package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.queue.DownloadRequest;
import com.norddev.downloadmanager.queue.RequestQueue;

public class Downloader {

    private final DownloaderInternal mInternalDownloader;
    private final DownloadTask.Callback mCallback = new DownloadTask.Callback() {
        @Override
        public void onTaskComplete(DownloadTask task) {
            mQueue.remove(task.getDownloadRequest());
            if (!processNext()) {
                shutdown();
            }
        }
    };
    private RequestQueue mQueue;
    private ShutdownListener mListener;

    public Downloader() {
        mInternalDownloader = new DownloaderInternal();
    }

    public boolean isRunning() {
        return mInternalDownloader.isStarted();
    }

    public void pause() {
        mInternalDownloader.pause();
    }

    public void resume() {
        mInternalDownloader.resume();
    }

    public void run(RequestQueue queue, ShutdownListener listener) {
        mQueue = queue;
        mListener = listener;
        mInternalDownloader.start();
        if (!processNext()) {
            shutdown();
        }
    }

    private boolean processNext() {
        DownloadRequest nextRequest = mQueue.peek();
        if(nextRequest != null) {
            DownloadTask task = new DownloadTask(nextRequest, mCallback);
            mInternalDownloader.process(task);
            return true;
        }
        return false;
    }

    private void shutdown() {
        mListener.onShutdown();
        mListener = null;
        mInternalDownloader.stop();
        mQueue = null;
    }

    public interface ShutdownListener {
        void onShutdown();
    }
}
