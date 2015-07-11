package com.norddev.downloadmanager.queue;

import android.support.annotation.NonNull;

public class RequestQueueProcessor {

    private final RequestQueue mRequestQueue;

    public interface QueueProcessorListener {
        void onNextRequest(DownloadRequest request);
        void onQueueProcessed();
    }

    public RequestQueueProcessor(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    public void processNext(@NonNull QueueProcessorListener listener) {
        DownloadRequest request = mRequestQueue.peek();
        if (request != null) {
            listener.onNextRequest(request);
        } else {
            listener.onQueueProcessed();
        }
    }
}
