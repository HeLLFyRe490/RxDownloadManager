package com.norddev.downloadmanager.cache.api;

import com.norddev.downloadmanager.cache.CacheFile;

import java.util.List;

public interface Cache {
    CacheFile getFile(String key);

    CacheFile createFile(String key, long fileSizeBytes);

    List<CacheFile> getFiles();

    void clear();
}
