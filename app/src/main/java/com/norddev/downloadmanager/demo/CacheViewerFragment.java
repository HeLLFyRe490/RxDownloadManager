package com.norddev.downloadmanager.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.cache.CacheFile;

import java.util.List;

/**
 *
 */
public class CacheViewerFragment extends Fragment {

    private DownloadManager mDownloadManager;
    private CacheFileAdapter mAdapter;
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
        View root = inflater.inflate(R.layout.fragment_cache_viewer, container, false);

        mAdapter = new CacheFileAdapter();
        mAdapter.setFiles(createItems());

        ListView list = (ListView) root.findViewById(R.id.fragment_cache_list);
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
        inflater.inflate(R.menu.menu_cache, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cache) {
            mDownloadManager.getCache().clear();
            reloadItems();
            Toast.makeText(getActivity(), "Cleared cache", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<CacheFile> createItems(){
        return mDownloadManager.getCache().getFiles();
    }

    private void reloadItems() {
        mAdapter.setFiles(createItems());
    }

    private void refreshItems() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void maybeRefreshItems(){
        if(mCount % 10 == 0){
            refreshItems();
            mCount = 0;
        }
        mCount++;
    }
}

