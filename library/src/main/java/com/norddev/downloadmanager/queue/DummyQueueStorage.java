package com.norddev.downloadmanager.queue;

import java.util.Collections;
import java.util.List;

/**
 * @param <E>
 */
public class DummyQueueStorage<E> implements QueueStorage<E> {

    @Override
    public void enqueue(E e) {
    }

    @Override
    public void shift(int fromPosition, int toPosition) {
    }

    @Override
    public boolean remove(E e) {
        return true;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public List<E> toList() {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
    }
}
