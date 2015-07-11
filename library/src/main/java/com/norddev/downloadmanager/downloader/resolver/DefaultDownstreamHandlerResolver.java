package com.norddev.downloadmanager.downloader.resolver;

import android.content.Context;

import com.norddev.downloadmanager.downloader.DefaultDownloadHandler;
import com.norddev.downloadmanager.downloader.DownloadHandler;

import java.io.File;

public class DefaultDownstreamHandlerResolver implements Resolver<DownloadHandler> {

    private File mOutputDirectory;

    public DefaultDownstreamHandlerResolver(Context context) {
        mOutputDirectory = context.getExternalFilesDir(null);
    }

    @Override
    public DownloadHandler resolve(int type) {
        return new DefaultDownloadHandler(mOutputDirectory);
    }
}
