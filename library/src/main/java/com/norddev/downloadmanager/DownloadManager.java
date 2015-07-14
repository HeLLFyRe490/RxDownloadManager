package com.norddev.downloadmanager;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.cache.Cache;
import com.norddev.downloadmanager.downloader.Downloader;
import com.norddev.downloadmanager.queue.api.DownloadRequestFactory;
import com.norddev.downloadmanager.queue.RequestQueue;

public class DownloadManager {

    private final DownloadRequestFactory mDownloadRequestFactory;
    private final RequestQueue mRequestQueue;
    private final Downloader mDownloader;
    private final Cache mCache;

    public DownloadManager(Configuration configuration) {
        mDownloadRequestFactory = configuration.mDownloadRequestFactory;
        mRequestQueue = new RequestQueue();
        mDownloader = new Downloader();
        mCache = new Cache();
    }

    @NonNull
    public DownloadRequestFactory getDownloadRequestFactory() {
        return mDownloadRequestFactory;
    }

    @NonNull
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    @NonNull
    public Cache getCache() {
        return mCache;
    }

    @NonNull
    public Downloader getDownloader() {
        return mDownloader;
    }

    public static class Configuration {
        private final DownloadRequestFactory mDownloadRequestFactory;

        public Configuration(Builder builder) {
            mDownloadRequestFactory = builder.mDownloadRequestFactory;
        }

        public static class Builder {
            private DownloadRequestFactory mDownloadRequestFactory;

            public void setDownloadRequestFactory(@NonNull DownloadRequestFactory downloadRequestFactory) {
                mDownloadRequestFactory = downloadRequestFactory;
            }

            public Configuration build() {
                return new Configuration(this);
            }
        }
    }
}
