package com.norddev.downloadmanager.downloader.api;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.queue.DownloadRequest;
import com.squareup.okhttp.Response;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DownloadHandler {
    void onRequestStarted(@NonNull DownloadRequest request) throws Exception;
    void onError(Exception e);
    void onBytesReceived(@NonNull byte[] buffer, int length) throws Exception;
    void onRequestFinished();
    long getPosition();
    long getContentSize();
    void onResponseReceived(Response response) throws IOException;
}
