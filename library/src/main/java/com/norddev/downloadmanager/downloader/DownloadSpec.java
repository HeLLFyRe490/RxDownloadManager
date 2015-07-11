package com.norddev.downloadmanager.downloader;

import android.net.Uri;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DownloadSpec {
    private String mKey;
    private String mURL;
    private final Map<String, String> mRequestHeaders;
    private int mRetryPolilcyType;
    private int mUpstreamHandlerType;
    private int mDownstreamHandlerType;

    public DownloadSpec(String key, String URL) {
        mKey = key;
        mURL = URL;
        mRequestHeaders = new LinkedHashMap<>();
    }

    public String getKey() {
        return mKey;
    }

    public String getURL() {
        return mURL;
    }

    public Map<String,String> getRequestHeaders(){
        return Collections.unmodifiableMap(mRequestHeaders);
    }

    public void addRequestHeader(String key, String value) {
        mRequestHeaders.put(key, value);
    }

    public void setRetryPolilcyType(int retryPolilcyType) {
        mRetryPolilcyType = retryPolilcyType;
    }

    public int getRetryPolilcyType() {
        return mRetryPolilcyType;
    }

    public void setDownstreamHandlerType(int downstreamHandlerType) {
        mDownstreamHandlerType = downstreamHandlerType;
    }

    public int getDownstreamHandlerType() {
        return mDownstreamHandlerType;
    }

    public void setUpstreamHandlerType(int upstreamHandlerType) {
        mUpstreamHandlerType = upstreamHandlerType;
    }

    public int getUpstreamHandlerType() {
        return mUpstreamHandlerType;
    }
}