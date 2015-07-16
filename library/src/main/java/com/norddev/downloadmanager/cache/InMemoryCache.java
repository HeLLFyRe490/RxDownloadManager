package com.norddev.downloadmanager.cache;

import com.norddev.downloadmanager.cache.api.Cache;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryCache implements Cache {

    private final Cache mSource;
    private final Map<String, CacheFile> mCacheFiles;

    public InMemoryCache() {
        this(null);
    }

    public InMemoryCache(Cache source) {
        mSource = source;
        mCacheFiles = new LinkedHashMap<>();
        if(mSource != null) {
            for (CacheFile file : mSource.getFiles()) {
                mCacheFiles.put(file.getKey(), file);
            }
        }
    }

    @Override
    public List<CacheFile> getFiles() {
        return Collections.unmodifiableList(new LinkedList<>(mCacheFiles.values()));
    }

    @Override
    public CacheFile getFile(String key) {
        return mCacheFiles.get(key);
    }

    @Override
    public CacheFile createFile(String key, long fileSizeBytes) {
        CacheFile cacheFile;
        if(mSource != null){
            cacheFile = mSource.createFile(key, fileSizeBytes);
        } else {
            cacheFile = new CacheFile(key, new File("/dev/null"), fileSizeBytes);
        }
        mCacheFiles.put(key, cacheFile);
        return cacheFile;
    }

    @Override
    public void clear() {
        if(mSource != null){
            mSource.clear();
        }
        mCacheFiles.clear();
    }

    public Cache getSource() {
        return mSource;
    }
}
