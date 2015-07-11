package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.Util;
import com.norddev.downloadmanager.queue.DownloadRequest;

import java.io.File;
import java.io.FileOutputStream;

public class DefaultDownloadHandler implements DownloadHandler {
    private File mDestinationDir;
    private FileOutputStream mOutput;
    private File mDestinationFile;

    public DefaultDownloadHandler(File destinationDir) {
        mDestinationDir = destinationDir;
    }

    @Override
    public void onRequest(DownloadRequest request) throws Exception {
        mDestinationFile = new File(mDestinationDir, request.getKey());
    }

    @Override
    public void onBytesReceived(byte[] buffer, int length) throws Exception {
        if(mOutput == null){
            mOutput = new FileOutputStream(mDestinationFile, true);
        }
        mOutput.write(buffer, 0, length);
    }

    @Override
    public long getPosition() {
        return mDestinationFile.length();
    }

    @Override
    public void onError(Exception e) {
        Util.closeQuietly(mOutput);
    }

    @Override
    public void onResponseFinished() {
        Util.closeQuietly(mOutput);
    }
}
