package com.norddev.downloadmanager.queue;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <E>
 */
public class InMemoryQueueStorage<E> implements QueueStorage<E> {

    private final LinkedList<E> mItems;
    private final QueueStorage<E> mSource;

    /**
     *
     */
    public InMemoryQueueStorage() {
        this(new DummyQueueStorage<E>());
    }

    /**
     * @param source
     */
    public InMemoryQueueStorage(@NonNull QueueStorage<E> source) {
        mSource = source;
        mItems = new LinkedList<>();
    }

    @Override
    public void enqueue(E e) {
        mSource.enqueue(e);
        mItems.addLast(e);
    }

    @Override
    public void shift(int fromPosition, int toPosition) {
        mSource.shift(fromPosition, toPosition);
        mItems.set(toPosition, mItems.get(fromPosition));
    }

    @Override
    public boolean remove(E e) {
        return mSource.remove(e) && mItems.remove(e);
    }

    @Override
    public List<E> toList() {
        return Collections.unmodifiableList(mItems);
    }

    @Override
    public E peek() {
        return mItems.getFirst();
    }

    @Override
    public void clear() {
        mItems.clear();
    }
}
