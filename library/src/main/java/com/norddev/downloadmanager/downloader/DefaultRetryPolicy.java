package com.norddev.downloadmanager.downloader;

import android.util.Log;

import com.norddev.downloadmanager.downloader.api.RetryPolicy;

import java.io.IOException;
import java.io.InterruptedIOException;

public class DefaultRetryPolicy implements RetryPolicy {

    private static final int DEFAULT_MAX_RETRY = 3;
    private static final String TAG = "DefaultRetryPolicy";
    private final int mMaxRetry;
    private int mAttempt;
    private boolean mIsErrorFatal;
    private boolean mSuccess;

    public DefaultRetryPolicy() {
        this(DEFAULT_MAX_RETRY);
    }

    public DefaultRetryPolicy(int maxRetry) {
        mMaxRetry = maxRetry;
    }

    @Override
    public boolean shouldRetry() {
        return !mSuccess && mAttempt < mMaxRetry && !mIsErrorFatal;
    }

    @Override
    public void onSuccess() {
        mSuccess = true;
    }

    @Override
    public void onError(Exception e) {
        boolean isFatal;
        if (e instanceof InterruptedIOException ||
                e instanceof HTTPResponseException ||
                e instanceof InterruptedException) {
            isFatal = true;
        } else if (e instanceof IOException) {
            mAttempt++;
            isFatal = false;
        } else {
            isFatal = true;
        }
        Log.v(TAG, "onError() " + e.getClass().getSimpleName() + " isFatal = " + isFatal);
        mIsErrorFatal = isFatal;
    }
}
