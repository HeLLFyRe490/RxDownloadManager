package com.norddev.downloadmanager.queue.api;

import com.norddev.downloadmanager.downloader.DownloadRequest;

import java.util.List;

/**
 *
 */
public interface QueueSource {

    /**
     * @param request
     */
    void enqueue(DownloadRequest request);

    /**
     * @param fromPosition
     * @param toPosition
     */
    void shift(int fromPosition, int toPosition);

    /**
     * @param key
     * @return
     */
    DownloadRequest remove(String key);

    /**
     * @return
     */
    DownloadRequest peek();

    /**
     * @return
     */
    List<DownloadRequest> toList();

    /**
     *
     */
    void clear();

    /**
     *
     * @param key
     * @return
     */
    boolean contains(String key);
}
