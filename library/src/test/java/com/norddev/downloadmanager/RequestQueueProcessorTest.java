package com.norddev.downloadmanager;

import com.norddev.downloadmanager.queue.RequestQueue;
import com.norddev.downloadmanager.queue.RequestQueueProcessor;

import junit.framework.TestCase;

public class RequestQueueProcessorTest extends TestCase {

    private RequestQueueProcessor mQueueProcessor;

    public void setUp() throws Exception {
        RequestQueue queue = new RequestQueue();
        mQueueProcessor = new RequestQueueProcessor(queue);
    }

    public void tearDown() throws Exception {
        mQueueProcessor = null;
    }

    public void testProcess() throws Exception {

    }
}