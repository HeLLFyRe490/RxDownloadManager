package com.norddev.downloadmanager.downloader;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.Util;
import com.norddev.downloadmanager.downloader.api.DownloadHandler;
import com.norddev.downloadmanager.queue.DownloadRequest;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 */
public class DefaultDownloadHandler implements DownloadHandler {
    private File mDestinationDir;
    private FileOutputStream mOutput;
    private File mDestinationFile;
    private long mContentSize;

    public DefaultDownloadHandler(File destinationDir) {
        mDestinationDir = destinationDir;
    }

    @Override
    public void onRequestStarted(@NonNull DownloadRequest request) throws Exception {
        mDestinationFile = new File(mDestinationDir, request.getKey());
    }

    @Override
    public void onBytesReceived(byte[] buffer, int length) throws Exception {
        mOutput.write(buffer, 0, length);
    }

    @Override
    public void onResponseReceived(@NonNull Response response) throws IOException {
        if(response.isSuccessful()){
            mOutput = new FileOutputStream(mDestinationFile, true);
        }
    }

    @Override
    public long getPosition() {
        return mDestinationFile.length();
    }

    @Override
    public void onError(@NonNull Exception e) {
        Util.closeQuietly(mOutput);
        mOutput = null;
    }

    @Override
    public void onRequestFinished() {
        Util.closeQuietly(mOutput);
        mOutput = null;
    }

    @Override
    public long getContentSize() {
        return mContentSize;
    }
}
