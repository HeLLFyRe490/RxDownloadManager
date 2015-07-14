package com.norddev.downloadmanager.queue.api;

import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.queue.DownloadRequest;

public interface DownloadRequestFactory {
    DownloadRequest createRequest(DownloadSpec spec);
}
