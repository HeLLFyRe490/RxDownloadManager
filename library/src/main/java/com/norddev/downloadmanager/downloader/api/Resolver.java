package com.norddev.downloadmanager.downloader.api;

public interface Resolver<T> {
    T resolve(int type);
}