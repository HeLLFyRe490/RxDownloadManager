package com.norddev.downloadmanager.queue;

import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.norddev.downloadmanager.queue.api.QueueSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class InMemoryQueueSource implements QueueSource {

    private final LinkedList<DownloadRequest> mRequests;
    private final QueueSource mSource;

    /**
     *
     */
    public InMemoryQueueSource() {
        this(null);
    }

    /**
     * @param source
     */
    public InMemoryQueueSource(QueueSource source) {
        mSource = source;
        if(mSource != null) {
            mRequests = new LinkedList<>(source.toList());
        } else {
            mRequests = new LinkedList<>();
        }
    }

    @Override
    public void enqueue(DownloadRequest e) {
        if(mSource != null) {
            mSource.enqueue(e);
        }
        mRequests.addLast(e);
    }

    @Override
    public void shift(int fromPosition, int toPosition) {
        if(mSource != null) {
            mSource.shift(fromPosition, toPosition);
        }
        mRequests.set(toPosition, mRequests.get(fromPosition));
    }

    @Override
    public DownloadRequest remove(String key) {
        DownloadRequest targetRequest = null;
        for(DownloadRequest request : mRequests){
            if(request.getKey().equals(key)){
                targetRequest = request;
            }
        }
        if(targetRequest != null){
            DownloadRequest removedRequest = null;
            if(mSource != null){
                removedRequest = mSource.remove(key);
            }
            if(removedRequest) {
                mRequests.remove(targetRequest);
            }
        }
        return targetRequest;
    }

    @Override
    public List<DownloadRequest> toList() {
        return Collections.unmodifiableList(mRequests);
    }

    @Override
    public DownloadRequest peek() {
        return mRequests.peek();
    }

    @Override
    public void clear() {
        if(mSource != null){
            mSource.clear();
        }
        mRequests.clear();
    }

    @Override
    public boolean contains(String key) {
        for(DownloadRequest request : mRequests){
            if(request.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }
}
