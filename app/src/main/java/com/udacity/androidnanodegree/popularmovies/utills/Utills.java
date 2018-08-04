/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;

/**
 * This is Util class for helper methods.
 */
public class Utills {
    public static boolean isConnectionAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
    public static String getYoutubeLink(String keyOfVideo){
        // Log.e(TAG, "getYoutubeLink: "+AppConstants.YOUTUBE_LINK.replace("{key_of_video}",keyOfVideo) );
        return AppConstants.YOUTUBE_LINK.replace("{key_of_video}",keyOfVideo);
    }
}
