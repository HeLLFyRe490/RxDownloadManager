package com.norddev.downloadmanager.downloader;

import java.io.IOException;

public class DefaultRetryPolicy implements RetryPolicy {

    private static final int MAX_RETRY = 3;
    private int mAttempt;
    private boolean mIsErrorFatal;
    private boolean mSuccess;

    @Override
    public boolean shouldRetry() {
        return !mSuccess && mAttempt < MAX_RETRY && !mIsErrorFatal;
    }

    @Override
    public void onSuccess() {
        mSuccess = true;
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof HTTPResponseException) {
            mIsErrorFatal = true;
        } else if (e instanceof IOException) {
            mAttempt++;
        } else {
            mIsErrorFatal = true;
        }
    }
}
