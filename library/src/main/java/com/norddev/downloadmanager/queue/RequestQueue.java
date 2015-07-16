package com.norddev.downloadmanager.queue;

import android.support.annotation.NonNull;

import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.norddev.downloadmanager.queue.api.QueueSource;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class RequestQueue {

    /**
     *
     */
    public interface Listener {
        /**
         * @param request
         */
        void onRequestAdded(DownloadRequest request);

        /**
         *
         * @param request
         */
        void onRequestRemoved(DownloadRequest request);
    }

    private final QueueSource mSource;
    private final CopyOnWriteArrayList<Listener> mListeners;

    /**
     * @param source
     */
    public RequestQueue(QueueSource source) {
        mSource = source;
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

    public boolean add(@NonNull DownloadRequest request){
        if(!contains(request.getKey())) {
            mSource.enqueue(request);
            notifyRequestAdded(request);
            return true;
        }
        return false;
    }

    public void shift(int fromPosition, int toPosition) {
        mSource.shift(fromPosition, toPosition);
    }

    public boolean remove(@NonNull String key){
        boolean success =  mSource.remove(key);
        if(success){
            notifyRequestRemoved(request);
        }
        return success;
    }

    public void clear(){
        mSource.clear();
    }

    public DownloadRequest peek(){
        return mSource.peek();
    }

    public List<DownloadRequest> toList() {
        return mSource.toList();
    }

    public boolean contains(String key) {
        return mSource.contains(key);
    }
}
