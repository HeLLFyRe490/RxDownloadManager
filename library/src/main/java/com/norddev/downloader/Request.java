package com.norddev.downloader;

import android.net.Uri;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
    private final Uri mUri;

    public final Map<String,String> mRequestProperties;

    public Request(Uri uri) {
        mUri = uri;
        mRequestProperties = new LinkedHashMap<>();
    }
}
