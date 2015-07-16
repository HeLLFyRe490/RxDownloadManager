package com.norddev.downloadmanager.cache;

import java.io.File;

public class CacheFile {
    private final String mKey;
    private final File mFile;
    private final long mFileSizeBytes;

    public CacheFile(String key, File file, long fileSizeBytes) {
        mKey = key;
        mFile = file;
        mFileSizeBytes = fileSizeBytes;
    }

    public String getKey() {
        return mKey;
    }

    public File getFile() {
        return mFile;
    }

    /**
     * Alias for getFile().length()
     * @return
     */
    public long getBytesDownloaded(){
        return mFile.length();
    }

    public long getFileSizeBytes() {
        return mFileSizeBytes;
    }
}
