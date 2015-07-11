package com.norddev.downloadmanager.downloader.resolver;

import com.norddev.downloadmanager.downloader.DefaultRetryPolicy;
import com.norddev.downloadmanager.downloader.RetryPolicy;

public class DefaultRetryPolicyResolver implements Resolver<RetryPolicy> {
    @Override
    public RetryPolicy resolve(int type) {
        return new DefaultRetryPolicy();
    }
}