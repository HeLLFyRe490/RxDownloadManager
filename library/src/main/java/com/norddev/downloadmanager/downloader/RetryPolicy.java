package com.norddev.downloadmanager.downloader;

public interface RetryPolicy {
    void onError(Exception e);
    void onSuccess();
    boolean shouldRetry();
}