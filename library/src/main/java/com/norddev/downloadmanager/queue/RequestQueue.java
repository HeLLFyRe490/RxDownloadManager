package com.norddev.downloadmanager.queue;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class RequestQueue {

    public interface Listener {
        void onRequestAdded(DownloadRequest request);
        void onRequestRemoved(DownloadRequest request);
    }

    private InMemoryQueueStorage<DownloadRequest> mQueueStorage;
    private final CopyOnWriteArrayList<Listener> mListeners;

    public RequestQueue() {
        mQueueStorage = new InMemoryQueueStorage<>();
        mListeners = new CopyOnWriteArrayList<>();
    }

    public void addListener(Listener listener){
        mListeners.add(listener);
    }

    public void removeListener(Listener listener){
        mListeners.remove(listener);
    }

    private void notifyRequestAdded(DownloadRequest request){
        for(Listener listener : mListeners){
            listener.onRequestAdded(request);
        }
    }

    private void notifyRequestRemoved(DownloadRequest request){
        for(Listener listener : mListeners){
            listener.onRequestRemoved(request);
        }
    }

    public void enqueue(@NonNull DownloadRequest request){
        mQueueStorage.enqueue(request);
        notifyRequestAdded(request);
    }

    public boolean remove(@NonNull DownloadRequest request){
        boolean success =  mQueueStorage.remove(request);
        if(success){
            notifyRequestRemoved(request);
        }
        return success;
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
