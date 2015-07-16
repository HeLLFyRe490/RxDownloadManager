package com.norddev.downloadmanager.demo;

public class QueueItem {
    private final String mKey;
    private final String mURL;
    private final DownloadStatus mStatus;

    public QueueItem(String key, String url) {
        mKey = key;
        mURL = url;
        mStatus = new DownloadStatus();
    }

    public DownloadStatus getStatus() {
        return mStatus;
    }

    public String getKey() {
        return mKey;
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof QueueItem && ((QueueItem) o).mKey.equals(mKey);
    }
}
