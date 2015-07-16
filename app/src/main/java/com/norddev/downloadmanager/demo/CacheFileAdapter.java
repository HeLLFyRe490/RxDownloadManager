package com.norddev.downloadmanager.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.norddev.downloadmanager.cache.CacheFile;
import com.norddev.downloadmanager.common.Util;
import com.norddev.downloadmanager.demo.R;

import java.util.List;

/**
 *
 */
public class CacheFileAdapter extends BaseAdapter {
    private List<CacheFile> mFiles;

    public void setFiles(List<CacheFile> files) {
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles != null ? mFiles.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cache_list_item, null, false);
        }
        CacheFile cacheFile = mFiles.get(position);

        TextView key = (TextView) convertView.findViewById(R.id.cache_list_item_key);
        key.setText(cacheFile.getKey());

        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.cache_list_item_progress_bar);
        progressBar.setProgress((int) (((double) cacheFile.getBytesDownloaded() / (double) cacheFile.getFileSizeBytes()) * 100));

        TextView progressText = (TextView) convertView.findViewById(R.id.cache_list_item_progress_text);
        progressText.setText(String.format("%s / %s", Util.formatFileSize(cacheFile.getBytesDownloaded()), Util.formatFileSize(cacheFile.getFileSizeBytes())));

        return convertView;
    }
}
