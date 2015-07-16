package com.norddev.downloadmanager.downloader;

import com.norddev.downloadmanager.downloader.api.DownloadHandler;

import junit.framework.TestCase;

import java.io.File;

public class DownloadTaskTest extends TestCase {

    private File mOutputDir;

    @Override
    protected void setUp() throws Exception {
        mOutputDir = new File("output");

        if (!mOutputDir.exists()) {
            mOutputDir.mkdir();
        }

        File[] files = mOutputDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public void testExecute() throws Exception {
        String key = "test";
        String url = "http://localhost/test_file";
        DownloadHandler downloadHandler = new DefaultDownloadHandler(mOutputDir);
        DownloadRequest request = new DownloadRequest(key, url, null, null, null, downloadHandler);
        DownloadTask task = new DownloadTask(callback, request);
        task.execute();
        assert (mOutputDir.listFiles().length > 0);
    }
}