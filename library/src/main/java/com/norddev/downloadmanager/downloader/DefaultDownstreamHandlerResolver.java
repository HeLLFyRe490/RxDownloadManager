package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.cache.DirectoryCache;
import com.norddev.downloadmanager.cache.InMemoryCache;
import com.norddev.downloadmanager.cache.api.Cache;
import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.norddev.downloadmanager.downloader.api.Resolver;

public class DefaultDownstreamHandlerResolver implements Resolver<DownloadHandler> {

    @Override
    public DownloadHandler resolve(int type) {
        return new DefaultDownloadHandler();
    }
}
