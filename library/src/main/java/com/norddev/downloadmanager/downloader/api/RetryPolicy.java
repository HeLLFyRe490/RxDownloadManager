package com.norddev.downloadmanager.downloader.api;

public interface RetryPolicy {
    void onError(Exception e);
    void onSuccess();
    boolean shouldRetry();
}