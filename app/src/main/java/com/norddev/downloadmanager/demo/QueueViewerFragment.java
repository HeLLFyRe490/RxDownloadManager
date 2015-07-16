package com.norddev.downloadmanager.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.cache.CacheFile;
import com.norddev.downloadmanager.common.C;
import com.norddev.downloadmanager.downloader.DownloadRequest;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class QueueViewerFragment extends Fragment {

    private DownloadManager mDownloadManager;
    private QueueItemAdapter mAdapter;
    private int mCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDownloadManager = DemoApplication.get().getDownloadManager();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_queue_viewer, container, false);

        final ToggleButton pauseButton = (ToggleButton) root.findViewById(R.id.activity_queue_pause_resume_button);
        pauseButton.setChecked(mDownloadManager.getDownloader().isPaused());
        pauseButton.setEnabled(DownloaderService.isRunning(getActivity()));
        pauseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //checked should be paused
                    mDownloadManager.getDownloader().pause();
                    Toast.makeText(getActivity(), "Downloader paused", Toast.LENGTH_SHORT).show();
                } else {
                    mDownloadManager.getDownloader().resume();
                    Toast.makeText(getActivity(), "Downloader resuming", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ToggleButton startButton = (ToggleButton) root.findViewById(R.id.activity_queue_start_shutdown_button);
        startButton.setChecked(DownloaderService.isRunning(getActivity()));
        startButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //checked should be running
                    pauseButton.setEnabled(true);
                    DownloaderService.start(getActivity());
                } else {
                    pauseButton.setEnabled(false);
                    pauseButton.setChecked(false);
                    DownloaderService.shutdown(getActivity());
                }
            }
        });

        mAdapter = new QueueItemAdapter();
        reloadItems();

        ListView list = (ListView) root.findViewById(R.id.activity_queue_list);
        list.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_queue, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_queue:
                AddToQueueDialogFragment dialog = new AddToQueueDialogFragment() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        super.onDismiss(dialog);
                        reloadItems();
                        refreshItems();
                    }
                };
                dialog.show(getChildFragmentManager(), "add_to_queue");
                return true;
            case R.id.action_clear_queue:
                mDownloadManager.getRequestQueue().clear();
                refreshItems();
                Toast.makeText(getActivity(), "Cleared queue", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadItems() {
        mAdapter.setItems(createItems());
    }

    private void refreshItems() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<QueueItem> createItems() {
        final List<QueueItem> items = new LinkedList<>();
        for (DownloadRequest request : mDownloadManager.getRequestQueue().toList()) {
            QueueItem item = new QueueItem(request.getKey(), request.getURL());
            CacheFile cacheFile = mDownloadManager.getCache().getFile(request.getKey());
            if (cacheFile != null) {
                item.getStatus().setBytesDownloaded(cacheFile.getBytesDownloaded());
                item.getStatus().setFileSizeBytes(cacheFile.getFileSizeBytes());
            }
            items.add(item);
        }
        return items;
    }

    public void setFileSizeBytes(DownloadRequest request, long contentLength) {
        DownloadStatus status = mAdapter.getStatus(request.getKey());
        if (status.getFileSizeBytes() == C.UNKNOWN) {
            status.setFileSizeBytes(contentLength);
            refreshItems();
        }
    }

    public void setBytesDownloaded(DownloadRequest request, long bytesDownloaded) {
        DownloadStatus status = mAdapter.getStatus(request.getKey());
        status.setBytesDownloaded(bytesDownloaded);
        if (mCount % 10 == 0) {
            refreshItems();
            mCount = 0;
        }
        mCount++;
    }
}
