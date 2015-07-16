package com.norddev.downloadmanager.downloader.api;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.cache.api.Cache;
import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.squareup.okhttp.Response;

import java.io.IOException;

public interface DownloadHandler {
    void onRequestStarted(@NonNull DownloadRequest request, Cache cache) throws Exception;

    void onError(Exception e);

    void onResponseReceived(Response response) throws IOException;

    void onBytesReceived(@NonNull byte[] buffer, int length) throws Exception;

    void onRequestFinished();

    long getPosition();

    long getFileSize();

}
