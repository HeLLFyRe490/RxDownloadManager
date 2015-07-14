package com.norddev.downloadmanager.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.norddev.downloadmanager.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class QueueItemAdapter extends BaseAdapter {

    private List<QueueItem> mItems;
    private final Map<String, DownloadStatus> mStatuses;

    public QueueItemAdapter() {
        mStatuses = new HashMap<>();
    }

    public void setItems(List<QueueItem> items){
        mItems = items;
        mStatuses.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_list_item, null, false);
        }
        QueueItem item = mItems.get(position);
        TextView key = (TextView) convertView.findViewById(R.id.queue_list_item_key);
        key.setText(item.mKey);
        TextView url = (TextView) convertView.findViewById(R.id.queue_list_item_url);
        url.setText(item.mURL);

        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.queue_list_item_progress_bar);
        TextView progressText = (TextView) convertView.findViewById(R.id.queue_list_item_progress_text);

        DownloadStatus status = mStatuses.get(item.mKey);
        if(status != null) {
            progressBar.setProgress((int) (((double)status.mBytesDownloaded / (double)status.mFileSizeBytes) * 100));
            progressText.setText(String.format("%s / %s", Util.formatFileSize(status.mBytesDownloaded), Util.formatFileSize(status.mFileSizeBytes)));
        } else {
            progressBar.setProgress(0);
            progressText.setText(String.format("%s / %s", "-", "-"));
        }
        return convertView;
    }

    public DownloadStatus getStatus(String key) {
        DownloadStatus status = mStatuses.get(key);
        if(status == null){
            status = new DownloadStatus();
            mStatuses.put(key, status);
        }
        return status;
    }
}
