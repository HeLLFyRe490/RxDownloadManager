package com.norddev.downloadmanager.queue;

import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.norddev.downloadmanager.downloader.api.RetryPolicy;

import java.util.Map;

public class DownloadRequest {
    private DownloadSpec mDownloadSpec;
    private final RetryPolicy mRetryPolicy;
    private final DownloadHandler mDownloadHandler;

    public DownloadRequest(DownloadSpec spec,
                           RetryPolicy retryPolicy,
                           DownloadHandler downloadHandler) {
        mDownloadSpec = spec;
        mRetryPolicy = retryPolicy;
        mDownloadHandler = downloadHandler;
    }

    public String getKey() {
        return mDownloadSpec.getKey();
    }

    public String getURL() {
        return mDownloadSpec.getURL();
    }

    public Map<String,String> getRequestHeaders() {
        return mDownloadSpec.getRequestHeaders();
    }

    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }

    public DownloadHandler getDownloadHandler() {
        return mDownloadHandler;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof DownloadRequest){
            return ((DownloadRequest) o).getKey().equals(getKey());
        }
        return false;
    }
}
