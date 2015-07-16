package com.norddev.downloadmanager.downloader;

import android.content.Context;

import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.norddev.downloadmanager.downloader.api.RetryPolicy;
import com.norddev.downloadmanager.downloader.api.Resolver;
import com.norddev.downloadmanager.downloader.api.DownloadRequestFactory;

public class DefaultDownloadRequestFactory implements DownloadRequestFactory {

    private final Resolver<RetryPolicy> mRetryPolicyResolver;
    private final Resolver<DownloadHandler> mDownstreamHandlerResolver;

    public DefaultDownloadRequestFactory() {
        mRetryPolicyResolver = new DefaultRetryPolicyResolver();
        mDownstreamHandlerResolver = new DefaultDownstreamHandlerResolver();
    }

    @Override
    public DownloadRequest createRequest(DownloadSpec spec){
        return new DownloadRequest(spec,
                mRetryPolicyResolver.resolve(spec.getRetryPolilcyType()),
                mDownstreamHandlerResolver.resolve(spec.getDownstreamHandlerType()));
    }

}
