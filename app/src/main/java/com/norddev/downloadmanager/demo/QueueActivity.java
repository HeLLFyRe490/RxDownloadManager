package com.norddev.downloadmanager.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.Util;
import com.norddev.downloadmanager.downloader.Downloader;
import com.norddev.downloadmanager.queue.DownloadRequest;
import com.norddev.downloadmanager.queue.RequestQueue;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class QueueActivity extends AppCompatActivity implements Downloader.Listener, RequestQueue.Listener {

    private DownloadManager mDownloadManager;
    private QueueItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        mDownloadManager = DemoApplication.get().getDownloadManager();
        mDownloadManager.getRequestQueue().addListener(this);
        mDownloadManager.getDownloader().addListener(this);
        DownloaderService.start(this);

        ToggleButton pauseButton = (ToggleButton) findViewById(R.id.activity_queue_pause_resume_button);
        pauseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDownloadManager.getDownloader().pause();
                    Toast.makeText(getApplicationContext(), "Downloader paused", Toast.LENGTH_SHORT).show();
                } else {
                    mDownloadManager.getDownloader().resume();
                    Toast.makeText(getApplicationContext(), "Downloader resuming", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter = new QueueItemAdapter();

        ListView list = (ListView) findViewById(R.id.activity_queue_list);
        list.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_queue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_queue:
                AddToQueueDialogFragment dialog = new AddToQueueDialogFragment(){
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        super.onDismiss(dialog);
                        refreshItems();
                    }
                };
                dialog.show(getSupportFragmentManager(), "add_to_queue");
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_clear_cache:
                Util.deleteDirectoryContents(getExternalFilesDir(null));
                refreshItems();
                Toast.makeText(this, "Cleared cache", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDownloadManager.getRequestQueue().removeListener(this);
        mDownloadManager.getDownloader().removeListener(this);
        DownloaderService.shutdown(this);
    }

    private void refreshItems() {
        List<QueueItem> items = new LinkedList<>();
        for (DownloadRequest request : mDownloadManager.getRequestQueue().toList()) {
            items.add(new QueueItem(request.getKey(), request.getURL()));
        }
        mAdapter.setItems(items);
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
        DownloadStatus status = mAdapter.getStatus(request.getKey());
        status.mFileSizeBytes = contentLength;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private int mCount = 0;

    @Override
    public void onDownloadProgress(DownloadRequest request, long bytesDownloaded) {
        DownloadStatus status = mAdapter.getStatus(request.getKey());
        status.mBytesDownloaded = bytesDownloaded;

        if(mCount % 10 == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
            mCount = 0;
        }
        mCount++;
    }

    @Override
    public void onDownloadError(DownloadRequest request, Exception error) {
        Toast.makeText(this, "Download error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
