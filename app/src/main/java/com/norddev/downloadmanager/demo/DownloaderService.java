package com.norddev.downloadmanager.demo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.RequestQueueProcessor;

/**
 *
 */
public class DownloaderService extends Service {

    private static final String ACTION_START = "com.norddev.downloadmanager.demo.START";
    private static final String ACTION_SHUTDOWN = "com.norddev.downloadmanager.demo.SHUTDOWN";

    private static final String TAG = "DownloaderService";
    private RequestQueueProcessor mRequestQueueProcessor;

    public static void start(Context context) {
        Intent i = new Intent(ACTION_START);
        i.setClass(context, DownloaderService.class);
        context.startService(i);
    }

    public static void shutdown(Context context) {
        Intent i = new Intent(ACTION_SHUTDOWN);
        i.setClass(context, DownloaderService.class);
        context.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate()");
        DownloadManager downloadManager = DemoApplication.get().getDownloadManager();
        mRequestQueueProcessor = new RequestQueueProcessor(downloadManager.getDownloader(), downloadManager.getRequestQueue());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        if (intent.getAction().equals(ACTION_START)) {
            if (!mRequestQueueProcessor.isRunning()) {
                mRequestQueueProcessor.start();
            }
        } else if (intent.getAction().equals(ACTION_SHUTDOWN)) {
            if (mRequestQueueProcessor.isRunning()) {
                mRequestQueueProcessor.shutdown();
                stopSelf();
            }
        } else {
            Log.w(TAG, "No action set for intent");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
