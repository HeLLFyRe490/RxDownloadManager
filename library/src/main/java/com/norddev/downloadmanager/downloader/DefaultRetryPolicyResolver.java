package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.downloader.api.Resolver;
import com.norddev.downloadmanager.downloader.api.RetryPolicy;

public class DefaultRetryPolicyResolver implements Resolver<RetryPolicy> {
    @Override
    public RetryPolicy resolve(int type) {
        return new DefaultRetryPolicy();
    }
}