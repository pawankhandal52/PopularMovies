/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.utills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is used to check the internet connection on device and broadcast the status
 */
public class NetworkReceiver extends BroadcastReceiver {
    private final NetworkListener mNetworkListener;
    
    public NetworkReceiver(NetworkListener networkListener) {
        this.mNetworkListener = networkListener;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
            if (isConnected) {
                mNetworkListener.connectionAvailable();
                
            } else {
                mNetworkListener.connectionUnAvailable();
            }
            }
    
    /**
     * This interface have two abstract methods which have tom implement when check the network
     */
    public interface NetworkListener {
        void connectionAvailable();
        
        void connectionUnAvailable();
    }
}
