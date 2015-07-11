package com.norddev.downloadmanager.downloader;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.queue.DownloadRequest;

public interface DownloadHandler {
    void onRequest(@NonNull DownloadRequest request) throws Exception;
    void onError(Exception e);
    void onBytesReceived(@NonNull byte[] buffer, int length) throws Exception;
    void onResponseFinished();
    long getPosition();
}
