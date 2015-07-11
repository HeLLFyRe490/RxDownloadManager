package com.norddev.downloadmanager.queue;

import android.support.annotation.NonNull;

import java.util.List;

/**
 *
 */
public class RequestQueue {

    private InMemoryQueueStorage<DownloadRequest> mQueueStorage;

    public RequestQueue() {
        mQueueStorage = new InMemoryQueueStorage<>();
    }

    public void enqueue(@NonNull DownloadRequest downloadRequest){
        mQueueStorage.enqueue(downloadRequest);
    }

    public boolean remove(@NonNull DownloadRequest request){
        return mQueueStorage.remove(request);
    }

    public void clear(){
        mQueueStorage.clear();
    }

    public DownloadRequest peek(){
        return mQueueStorage.peek();
    }

    public List<DownloadRequest> toList() {
        return mQueueStorage.toList();
    }
}
