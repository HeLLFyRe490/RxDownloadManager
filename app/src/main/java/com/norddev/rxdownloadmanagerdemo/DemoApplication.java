package com.norddev.rxdownloadmanagerdemo;

import android.app.Application;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.queue.DefaultDownloadRequestFactory;

public class DemoApplication extends Application {

    private static DemoApplication sInstance;

    private DownloadManager mDownloadManager;

    @Override
    public void onCreate() {
        sInstance = this;
        DownloadManager.Configuration.Builder builder = new DownloadManager.Configuration.Builder();
        builder.setDownloadRequestFactory(new DefaultDownloadRequestFactory(this));
        mDownloadManager = new DownloadManager(builder.build());
    }

    public static DemoApplication get(){
        return sInstance;
    }

    public DownloadManager getDownloadManager() {
        return mDownloadManager;
    }
}
