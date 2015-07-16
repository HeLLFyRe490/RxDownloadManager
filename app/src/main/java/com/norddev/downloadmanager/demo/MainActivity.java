package com.norddev.downloadmanager.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.norddev.downloadmanager.downloader.Downloader;
import com.norddev.downloadmanager.queue.RequestQueue;

/**
 * Activity hosting the queue and cache view tabs and piping download events to them
 */
public class MainActivity extends AppCompatActivity implements Downloader.Listener, RequestQueue.Listener {

    private DownloadManager mDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("queue").setIndicator("Queue"), QueueViewerFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("cache").setIndicator("Cache"), CacheViewerFragment.class, null);

        mDownloadManager = DemoApplication.get().getDownloadManager();
        mDownloadManager.getRequestQueue().addListener(this);
        mDownloadManager.getDownloader().addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDownloadManager.getRequestQueue().removeListener(this);
        mDownloadManager.getDownloader().removeListener(this);
    }

    @Override
    public void onDownloaderStarted() {
        Toast.makeText(this, "Downloader started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadComplete(DownloadRequest request) {
        Toast.makeText(this, "Download complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadResponseReceived(DownloadRequest request, long contentLength) {
        QueueViewerFragment queue = (QueueViewerFragment) getSupportFragmentManager().findFragmentByTag("queue");
        if(queue != null) {
            queue.setFileSizeBytes(request, contentLength);
        }
    }

    @Override
    public void onDownloadProgress(DownloadRequest request, long bytesDownloaded) {
        QueueViewerFragment queue = (QueueViewerFragment) getSupportFragmentManager().findFragmentByTag("queue");
        if(queue != null) {
            queue.setBytesDownloaded(request, bytesDownloaded);
        }

        CacheViewerFragment cache = (CacheViewerFragment) getSupportFragmentManager().findFragmentByTag("cache");
        if(cache != null) {
            cache.maybeRefreshItems();
        }
    }

    @Override
    public void onDownloadError(DownloadRequest request, Exception error) {
        Toast.makeText(this, "Download error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloaderShutdown() {
        Toast.makeText(this, "Downloader shutdown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadInterrupted() {
        Toast.makeText(this, "Download interrupted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestAdded(DownloadRequest request) {
        Toast.makeText(this, "Request added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestRemoved(DownloadRequest request) {
        Toast.makeText(this, "Request removed", Toast.LENGTH_SHORT).show();
    }
}
