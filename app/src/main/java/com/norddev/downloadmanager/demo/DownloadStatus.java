package com.norddev.downloadmanager.demo;

import com.norddev.downloadmanager.common.C;

public class DownloadStatus {
    private long mBytesDownloaded = 0;
    private long mFileSizeBytes = C.UNKNOWN;

    public void setBytesDownloaded(long bytesDownloaded) {
        mBytesDownloaded = bytesDownloaded;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        mFileSizeBytes = fileSizeBytes;
    }

    public long getBytesDownloaded() {
        return mBytesDownloaded;
    }

    public long getFileSizeBytes() {
        return mFileSizeBytes;
    }
}
