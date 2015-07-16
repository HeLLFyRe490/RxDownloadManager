package com.norddev.downloadmanager.demo;

import android.app.Application;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.cache.InMemoryCache;
import com.norddev.downloadmanager.cache.DirectoryCache;
import com.norddev.downloadmanager.downloader.DefaultDownloadRequestFactory;
import com.norddev.downloadmanager.queue.InMemoryQueueSource;

/**
 *
 */
public class DemoApplication extends Application {

    private static DemoApplication sInstance;

    private DownloadManager mDownloadManager;

    @Override
    public void onCreate() {
        super.onCreate();

        new TestHTTPServer().start();

        sInstance = this;
        DownloadManager.Configuration.Builder builder = new DownloadManager.Configuration.Builder();
        builder.setDownloadRequestFactory(new DefaultDownloadRequestFactory());
        builder.setCache(new InMemoryCache(new DirectoryCache(getExternalFilesDir(null))));
        builder.setQueueSource(new InMemoryQueueSource());
        mDownloadManager = new DownloadManager(builder.build());
    }

    /**
     * @return The global application instance
     */
    public static DemoApplication get(){
        return sInstance;
    }

    public DownloadManager getDownloadManager() {
        return mDownloadManager;
    }
}
