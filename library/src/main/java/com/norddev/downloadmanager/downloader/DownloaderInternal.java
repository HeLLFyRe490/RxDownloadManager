package com.norddev.downloadmanager.downloader;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class DownloaderInternal implements Handler.Callback {

    public static final int PAUSE_EVENT = 0;
    public static final int RESUME_EVENT = 1;
    public static final int DOWNLOAD_EVENT = 2;

    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 1;
    private static final int STATE_STOPPED = 2;
    private static final int STATE_DONWLOADING = 3;

    private DownloaderThread mThread;
    private Handler mHandler;
    private volatile int mState;
    private volatile DownloadTask mCurrentTask;

    public DownloaderInternal() {
        setState(STATE_STOPPED);
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void start(){
        if(!isStarted()){
            mThread = new DownloaderThread();
            mThread.start();
            mHandler = new Handler(mThread.getLooper());
            setState(STATE_IDLE);
        }
    }

    public synchronized void pause() {
        if(isStarted()) {
            mHandler.sendEmptyMessage(PAUSE_EVENT);
        }
    }

    public synchronized void resume() {
        if(isStarted()) {
            mHandler.sendEmptyMessage(RESUME_EVENT);
        }
    }

    public synchronized void process(DownloadTask task){
        if(isStarted()) {
            Message.obtain(mHandler, DOWNLOAD_EVENT, task).sendToTarget();
        }
    }

    public synchronized void stop(){
        if(isStarted()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                mThread.quitSafely();
            } else {
                mThread.quit();
            }
            setState(STATE_STOPPED);
            mHandler = null;
            mThread = null;
        }
    }

    public synchronized boolean isStarted(){
        return mHandler != null;
    }

    private void setState(int state){
        mState = state;
    }

    private void setState(int state, DownloadTask task){
        mState = state;
        mCurrentTask = task;
    }

    private void clearTask(){
        mCurrentTask = null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case PAUSE_EVENT:
                setState(STATE_PAUSED, mCurrentTask);
                break;
            case RESUME_EVENT:
                if(mCurrentTask != null){
                    process(mCurrentTask);
                } else {
                    setState(STATE_IDLE);
                }
                break;
            case DOWNLOAD_EVENT:
                DownloadTask task = (DownloadTask) msg.obj;
                setState(STATE_DONWLOADING, task);
                task.execute();
                clearTask();
                break;
        }
        return false;
    }

    private static class DownloaderThread extends HandlerThread {
        public DownloaderThread() {
            super("DownloaderThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }
    }
}
