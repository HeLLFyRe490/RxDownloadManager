package com.norddev.rxdownloadmanagerdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 *
 */
public class DownloadItemAdapter extends BaseAdapter {

    private List<DownloadItem> mItems;

    public void setItems(List<DownloadItem> items){
        mItems = items;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abc_list_menu_item_layout, null, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(mItems.get(position).mKey);
        return convertView;
    }
}
