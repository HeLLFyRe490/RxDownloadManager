package com.norddev.downloadmanager.queue;

import android.content.Context;

import com.norddev.downloadmanager.downloader.DownloadHandler;
import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.downloader.RetryPolicy;
import com.norddev.downloadmanager.downloader.resolver.DefaultDownstreamHandlerResolver;
import com.norddev.downloadmanager.downloader.resolver.DefaultRetryPolicyResolver;
import com.norddev.downloadmanager.downloader.resolver.Resolver;

public class DefaultDownloadRequestFactory implements DownloadRequestFactory {

    private final Resolver<RetryPolicy> mRetryPolicyResolver;
    private final Resolver<DownloadHandler> mDownstreamHandlerResolver;

    public DefaultDownloadRequestFactory(Context context) {
        mRetryPolicyResolver = new DefaultRetryPolicyResolver();
        mDownstreamHandlerResolver = new DefaultDownstreamHandlerResolver(context);
    }

    @Override
    public DownloadRequest createRequest(DownloadSpec spec){
        return new DownloadRequest(spec,
                mRetryPolicyResolver.resolve(spec.getRetryPolilcyType()),
                mDownstreamHandlerResolver.resolve(spec.getDownstreamHandlerType()));
    }

}
