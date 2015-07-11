package com.norddev.rxdownloadmanagerdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.Downloader;
import com.norddev.downloadmanager.queue.RequestQueue;

public class DownloaderService extends Service implements Downloader.ShutdownListener {

    private static final String TAG = "DownloaderService";
    private Downloader mDownloader;
    private RequestQueue mQueue;

    public static void start(Context context) {
        context.startService(new Intent(context, DownloaderService.class));
    }

    @Override
    public void onCreate() {
        DownloadManager downloadManager = DemoApplication.get().getDownloadManager();
        mDownloader = downloadManager.getDownloader();
        mQueue = downloadManager.getRequestQueue();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service starting");
        invokeDownloader();
        return START_STICKY;
    }

    private void invokeDownloader(){
        if (!mDownloader.isRunning()) {
            mDownloader.run(mQueue, this);
        }
    }

    @Override
    public void onShutdown() {
        Log.i(TAG, "Service stopping");
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
