package com.norddev.downloadmanager.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //DemoApplication.get().getDownloadManager().getDownloader().onConnectionUpdate();
    }
}
