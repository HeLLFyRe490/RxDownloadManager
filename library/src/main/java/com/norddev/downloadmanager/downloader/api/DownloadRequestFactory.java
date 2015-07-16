package com.norddev.downloadmanager.downloader.api;

import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.downloader.DownloadRequest;

public interface DownloadRequestFactory {
    DownloadRequest createRequest(DownloadSpec spec);
}
