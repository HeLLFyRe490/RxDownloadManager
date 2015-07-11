package com.norddev.rxdownloadmanagerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.queue.DownloadRequest;
import com.norddev.downloadmanager.queue.DownloadRequestFactory;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class QueueActivity extends AppCompatActivity {

    private DownloadManager mDownloadManager;
    private DownloadItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDownloadManager = DemoApplication.get().getDownloadManager();
        mAdapter = new DownloadItemAdapter();

        ListView list = (ListView) findViewById(R.id.activity_main_list);
        list.setAdapter(mAdapter);

        DownloadSpec spec = new DownloadSpec("key", "http://www.google.com");
        DownloadRequestFactory factory = mDownloadManager.getDownloadRequestFactory();
        DownloadRequest request = factory.createRequest(spec);
        mDownloadManager.getRequestQueue().enqueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }

    private void refreshItems() {
        List<DownloadItem> items = new LinkedList<>();
        for (DownloadRequest request : mDownloadManager.getRequestQueue().toList()) {
            items.add(new DownloadItem(request.getKey()));
        }
        mAdapter.setItems(items);
    }
}
