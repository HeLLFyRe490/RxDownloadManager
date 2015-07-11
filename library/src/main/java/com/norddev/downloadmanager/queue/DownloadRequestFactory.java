package com.norddev.downloadmanager.queue;

import com.norddev.downloadmanager.downloader.DownloadSpec;

public interface DownloadRequestFactory {
    DownloadRequest createRequest(DownloadSpec spec);
}
