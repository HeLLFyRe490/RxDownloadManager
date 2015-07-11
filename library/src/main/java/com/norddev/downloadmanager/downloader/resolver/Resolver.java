package com.norddev.downloadmanager.downloader.resolver;

public interface Resolver<T> {
    T resolve(int type);
}