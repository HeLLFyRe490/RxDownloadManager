//package com.norddev.downloadmanager;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//public final class ConnectivityMonitor {
//
//    private static final BehaviorSubject<NetworkInfo> sBus = BehaviorSubject.create();
//
//    public static Subscription subscribe(Action1<NetworkInfo> subscriber){
//        return sBus.subscribe(subscriber);
//    }
//
//    public static NetworkInfo getCurrentNetworkInfo(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cm.getActiveNetworkInfo();
//    }
//
//    public static boolean hasActiveConnection(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = cm.getActiveNetworkInfo();
//        return info != null && info.isConnected();
//    }
//
//    public static class Recevier extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            NetworkInfo info = getCurrentNetworkInfo(context);
//            sBus.onNext(info);
//        }
//    }
//
//}
