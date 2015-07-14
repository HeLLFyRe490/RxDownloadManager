package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.queue.DownloadRequest;
import com.squareup.okhttp.Response;

import java.util.concurrent.CopyOnWriteArrayList;

public class Downloader {

    private final DownloaderInternal mDownloaderInternal;
    private final CopyOnWriteArrayList<Listener> mListeners;

    public Downloader() {
        mDownloaderInternal = new DownloaderInternal();
        mListeners = new CopyOnWriteArrayList<>();
    }

    public boolean isRunning() {
        return mDownloaderInternal.isRunning();
    }

    public void start() {
        mDownloaderInternal.start();
        notifyStarted();
    }

    public void shutdown() {
        mDownloaderInternal.stop();
        notifyShutdown();
    }

    public void interrupt(){
        mDownloaderInternal.interrupt();
        notifyInterrupted();
    }

    public void pause() {
        mDownloaderInternal.pause();
    }

    public void resume() {
        mDownloaderInternal.resume();
    }

    public void execute(DownloadRequest request) {
        DownloadTask task = new DownloadTask(request, new DownloadTask.Callback() {
            @Override
            public void onComplete(DownloadTask task) {
                notifyComplete(task.getDownloadRequest());
            }

            @Override
            public void onProgress(DownloadTask task, long bytes) {
                notifyProgress(task.getDownloadRequest(), bytes);
            }

            @Override
            public void onError(DownloadTask task, Exception e) {
                notifyError(task.getDownloadRequest(), e);
            }

            @Override
            public void onResponse(DownloadTask task, long contentLength) {
                notifyResponseReceived(task.getDownloadRequest(), contentLength);
            }
        });
        mDownloaderInternal.process(task);
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    private void notifyStarted() {
        for (Listener listener : mListeners) {
            listener.onDownloaderStarted();
        }
    }

    private void notifyShutdown() {
        for (Listener listener : mListeners) {
            listener.onDownloaderShutdown();
        }
    }

    private void notifyProgress(DownloadRequest request, long bytesDownloaded) {
        for (Listener listener : mListeners) {
            listener.onDownloadProgress(request, bytesDownloaded);
        }
    }

    private void notifyComplete(DownloadRequest request) {
        for (Listener listener : mListeners) {
            listener.onDownloadComplete(request);
        }
    }

    private void notifyError(DownloadRequest request, Exception error) {
        for (Listener listener : mListeners) {
            listener.onDownloadError(request, error);
        }
    }

    private void notifyInterrupted() {
        for (Listener listener : mListeners) {
            listener.onDownloadInterrupted();
        }
    }

    private void notifyResponseReceived(DownloadRequest request, long contentLength) {
        for (Listener listener : mListeners) {
            listener.onDownloadResponseReceived(request, contentLength);
        }
    }

    public interface Listener {
        void onDownloaderStarted();

        void onDownloadComplete(DownloadRequest request);

        void onDownloadResponseReceived(DownloadRequest request, long contentLength);

        void onDownloadProgress(DownloadRequest request, long bytesDownloaded);

        void onDownloadError(DownloadRequest request, Exception error);

        void onDownloaderShutdown();

        void onDownloadInterrupted();
    }
}
