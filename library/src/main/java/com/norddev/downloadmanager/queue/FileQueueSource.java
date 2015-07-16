package com.norddev.downloadmanager.queue;

import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.norddev.downloadmanager.queue.api.QueueSource;

import java.util.List;

/**
 *
 */
public class FileQueueSource implements QueueSource {
    @Override
    public void enqueue(DownloadRequest request) {

    }

    @Override
    public void shift(int fromPosition, int toPosition) {

    }

    @Override
    public boolean remove(DownloadRequest request) {
        return false;
    }

    @Override
    public DownloadRequest peek() {
        return null;
    }

    @Override
    public List toList() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(String key) {
        return false;
    }
}
