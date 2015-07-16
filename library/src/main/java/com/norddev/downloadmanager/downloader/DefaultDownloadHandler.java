package com.norddev.downloadmanager.downloader;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.cache.CacheFile;
import com.norddev.downloadmanager.cache.api.Cache;
import com.norddev.downloadmanager.common.C;
import com.norddev.downloadmanager.common.Util;
import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.squareup.okhttp.Response;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class DefaultDownloadHandler implements DownloadHandler {
    private Cache mCache;
    private FileOutputStream mOutput;
    private CacheFile mCurrentCacheFile;
    private DownloadRequest mCurrentRequest;

    @Override
    public void onRequestStarted(@NonNull DownloadRequest request, Cache cache) throws Exception {
        mCurrentRequest = request;
        mCache = cache;
        mCurrentCacheFile = mCache.getFile(mCurrentRequest.getKey());
    }

    @Override
    public void onBytesReceived(byte[] buffer, int length) throws Exception {
        mOutput.write(buffer, 0, length);
    }

    @Override
    public void onResponseReceived(@NonNull Response response) throws IOException {
        if (response.isSuccessful()) {
            if(mCurrentCacheFile == null){
                mCurrentCacheFile = mCache.createFile(mCurrentRequest.getKey(), response.body().contentLength());
            }
            mOutput = new FileOutputStream(mCurrentCacheFile.getFile(), true);
        }
    }

    @Override
    public long getPosition() {
        return mCurrentCacheFile != null ? mCurrentCacheFile.getBytesDownloaded() : 0;
    }

    @Override
    public void onError(@NonNull Exception e) {
        cleanup();
    }

    @Override
    public void onRequestFinished() {
        cleanup();
    }

    private void cleanup() {
        Util.closeQuietly(mOutput);
        mOutput = null;
        mCurrentCacheFile = null;
        mCurrentRequest = null;
    }

    @Override
    public long getFileSize() {
        return mCurrentCacheFile != null ? mCurrentCacheFile.getFileSizeBytes() : C.UNKNOWN;
    }
}
