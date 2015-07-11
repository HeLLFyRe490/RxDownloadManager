package com.norddev.downloadmanager.queue;

import java.util.List;

public interface QueueStorage<E> {

    /**
     * @param e
     */
    void enqueue(E e);

    /**
     * @param fromPosition
     * @param toPosition
     */
    void shift(int fromPosition, int toPosition);

    /**
     * @param e
     * @return
     */
    boolean remove(E e);

    /**
     * @return
     */
    E peek();

    /**
     * @return
     */
    List<E> toList();

    /**
     *
     */
    void clear();
}
