package com.norddev.downloadmanager.downloader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.norddev.downloadmanager.common.EventLoop;

/**
 *
 */
public class DownloaderInternal {

    private static final String TAG = "DownloaderInternal";

    private final EventLoop mDownloadLoop;
    private final EventLoop mEventLoop;
    private final DownloadEventLoopCallback mCallback;

    public DownloaderInternal() {
        mCallback = new DownloadEventLoopCallback();
        mEventLoop = new EventLoop("DownloadEventLoop", mCallback);
        mDownloadLoop = new EventLoop("DownloadLoop", new DownloadLoopCallback());
    }

    public void start() {
        if (!isRunning()) {
            mEventLoop.start();
            mEventLoop.post(DownloadEventLoopCallback.START_EVENT);
        }
    }

    public void pause() {
        if (isRunning()) {
            mEventLoop.post(DownloadEventLoopCallback.PAUSE_EVENT);
        }
    }

    public void resume() {
        if (isRunning()) {
            mEventLoop.post(DownloadEventLoopCallback.RESUME_EVENT);
        }
    }

    public void process(DownloadTask task) {
        if (isRunning()) {
            mEventLoop.post(DownloadEventLoopCallback.START_DOWNLOAD_EVENT, task);
        }
    }

    public void stop() {
        if (isRunning()) {
            mEventLoop.post(DownloadEventLoopCallback.STOP_EVENT);
            mEventLoop.quit();
        }
    }

    public void interrupt() {
        if (isRunning()) {
            mEventLoop.post(DownloadEventLoopCallback.INTERRUPT_EVENT);
        }
    }

    public boolean isRunning() {
        return mEventLoop.isRunning();
    }

    public boolean isPaused() {
        return mCallback.getState() == DownloadEventLoopCallback.STATE_PAUSED;
    }

    private class DownloadLoopCallback implements Handler.Callback {
        private static final int DOWNLOAD_EVENT = 0;

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_EVENT:
                    DownloadTask task = (DownloadTask) msg.obj;
                    task.execute();
                    mEventLoop.post(DownloadEventLoopCallback.FINISH_DOWNLOAD_EVENT);
                    break;
            }
            return false;
        }
    }

    private class DownloadEventLoopCallback implements Handler.Callback {

        private static final int PAUSE_EVENT = 0;
        private static final int RESUME_EVENT = 1;
        private static final int START_DOWNLOAD_EVENT = 2;
        private static final int FINISH_DOWNLOAD_EVENT = 3;
        private static final int INTERRUPT_EVENT = 4;
        private static final int START_EVENT = 5;
        private static final int STOP_EVENT = 6;

        private static final int STATE_IDLE = 0;
        private static final int STATE_PAUSED = 1;
        private static final int STATE_STOPPED = 2;
        private static final int STATE_DOWNLOADING = 3;

        private volatile int mState;
        private DownloadTask mCurrentTask;

        public DownloadEventLoopCallback() {
            setState(STATE_STOPPED, null);
        }

        public int getState() {
            return mState;
        }

        private void setState(int state, DownloadTask task) {
            Log.d(TAG, "State change: " + stringForState(mState) + " -> " + stringForState(state));
            mState = state;
            if(mCurrentTask != null && task == null){
                Log.d(TAG, "Clearing current task");
            } else if(mCurrentTask == null && task != null){
                Log.d(TAG, "Setting current task");
            }
            mCurrentTask = task;
        }

        private String stringForState(int state){
            switch (state){
                case STATE_DOWNLOADING:
                    return "DOWNLOADING";
                case STATE_IDLE:
                    return "IDLE";
                case STATE_PAUSED:
                    return "PAUSED";
                case STATE_STOPPED:
                    return "STOPPED";
                default:
                    return "UNKNOWN";
            }
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_EVENT:
                    if (mState == STATE_STOPPED) {
                        mDownloadLoop.start();
                        setState(STATE_IDLE, null);
                    } else {
                        //STATE_IDLE
                        //STATE_PAUSED
                        //STATE_DOWNLOADING
                    }
                    break;
                case STOP_EVENT:
                    if (mState != STATE_STOPPED) {
                        if (mCurrentTask != null) {
                            mCurrentTask.interrupt();
                        }
                        setState(STATE_STOPPED, null);
                        mDownloadLoop.quit();
                    } else {
                        //STATE_IDLE
                        //STATE_PAUSED
                        //STATE_DOWNLOADING
                    }
                    break;
                case PAUSE_EVENT:
                    if (mState == STATE_IDLE ||
                            mState == STATE_DOWNLOADING) {
                        if (mCurrentTask != null) {
                            mCurrentTask.interrupt();
                        }
                        setState(STATE_PAUSED, mCurrentTask);
                    } else {
                        //STATE_PAUSED
                        //STATE_STOPPED
                    }
                    break;
                case RESUME_EVENT:
                    if (mState == STATE_PAUSED) {
                        if (mCurrentTask != null) {
                            setState(STATE_DOWNLOADING, mCurrentTask);
                            mDownloadLoop.post(DownloadLoopCallback.DOWNLOAD_EVENT, mCurrentTask);
                        } else {
                            setState(STATE_IDLE, null);
                        }
                    } else {
                        //STATE_IDLE
                        //STATE_`WNLOADING
                        //STATE_STOPPED
                    }
                    break;
                case START_DOWNLOAD_EVENT:
                    DownloadTask task = (DownloadTask) msg.obj;
                    if (mState == STATE_PAUSED) {
                        setState(STATE_PAUSED, task);
                    } else if (mState == STATE_IDLE ||
                            mState == STATE_DOWNLOADING) {
                        setState(STATE_DOWNLOADING, task);
                        mDownloadLoop.post(DownloadLoopCallback.DOWNLOAD_EVENT, task);
                    } else {
                        //STATE_STOPPED
                    }
                    break;
                case FINISH_DOWNLOAD_EVENT:
                    if (mState == STATE_DOWNLOADING) {
                        setState(STATE_IDLE, null);
                    } else {
                        //STATE_IDLE
                        //STATE_PAUSED
                        //STATE_STOPPED
                    }
                    break;
                case INTERRUPT_EVENT:
                    if (mState == STATE_DOWNLOADING) {
                        mCurrentTask.interrupt();
                        setState(STATE_IDLE, null);
                    } else {
                        //STATE_IDLE
                        //STATE_PAUSED
                        //STATE_STOPPED
                    }
                    break;
            }
            return false;
        }
    }

}
