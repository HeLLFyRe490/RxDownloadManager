package com.norddev.downloadmanager;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 *
 */
public class EventLoop {
    private static final String TAG = "EventLoop";
    private final String mName;
    private HandlerThread mThread;
    private Handler.Callback mCallback;
    private Handler mHandler;

    public EventLoop(String name, Handler.Callback callback) {
        mName = name;
        mCallback = callback;
    }

    public void start() {
        Log.v(TAG, "Starting " + toString());
        mThread = new EventLoopThread(mName);
        mThread.start();
        mHandler = new Handler(mThread.getLooper(), mCallback);
    }

    public void quit() {
        Log.v(TAG, "Quitting " + toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mThread.quitSafely();
        } else {
            mThread.quit();
        }
        mHandler = null;
        mThread = null;
    }

    public void post(int eventType) {
        post(eventType, null);
    }

    public void post(int eventType, Object extra) {
        if (isRunning()) {
            Log.v(TAG, "Posting to " + toString() + " - " + eventType + (extra != null ? " - " + extra : ""));
            Message.obtain(mHandler, eventType, extra).sendToTarget();
        } else {
            Log.w(TAG, toString() + " has not been started");
        }
    }

    @Override
    public String toString() {
        return "EventLoop: " + mName;
    }

    public boolean isRunning() {
        return mHandler != null;
    }

    private static class EventLoopThread extends HandlerThread {
        public EventLoopThread(String name) {
            super(name, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }
    }

}