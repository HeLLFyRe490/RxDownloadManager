package com.norddev.downloadmanager;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.cache.api.Cache;
import com.norddev.downloadmanager.downloader.Downloader;
import com.norddev.downloadmanager.downloader.api.DownloadRequestFactory;
import com.norddev.downloadmanager.queue.RequestQueue;
import com.norddev.downloadmanager.queue.api.QueueSource;

public class DownloadManager {

    private final DownloadRequestFactory mDownloadRequestFactory;
    private final RequestQueue mRequestQueue;
    private final Downloader mDownloader;
    private final Cache mCache;

    public DownloadManager(Configuration configuration) {
        mDownloadRequestFactory = configuration.mDownloadRequestFactory;
        mRequestQueue = new RequestQueue(configuration.mQueueSource);
        mCache = configuration.mCache;
        mDownloader = new Downloader(mCache);
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

    /**
     *
     */
    public static class Configuration {
        private final DownloadRequestFactory mDownloadRequestFactory;
        private final Cache mCache;
        private final QueueSource mQueueSource;

        private Configuration(Builder builder) {
            mDownloadRequestFactory = builder.mDownloadRequestFactory;
            mCache = builder.mCache;
            mQueueSource = builder.mQueueSource;
        }

        /**
         *
         */
        public static class Builder {
            private DownloadRequestFactory mDownloadRequestFactory;
            private Cache mCache;
            private QueueSource mQueueSource;

            public void setDownloadRequestFactory(@NonNull DownloadRequestFactory downloadRequestFactory) {
                mDownloadRequestFactory = downloadRequestFactory;
            }

            public void setCache(@NonNull Cache cache){
                mCache = cache;
            }

            public void setQueueSource(@NonNull QueueSource queueSource){
                mQueueSource = queueSource;
            }

            /**
             * @return The new Configuration instance
             */
            public Configuration build() {
                return new Configuration(this);
            }
        }
    }
}
