/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.networking;

import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is used to build the retrofit client which help to make a network calls
 */
public class ConfigApi {
    private static Retrofit sRetrofit = null;
    
    /**
     * This method is used to get the Retrofit build object.
     *
     * @return Retrofit object after building.
     */
    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppConstants.BASE_URL)
                    .build();
        }
        return sRetrofit;
    }
    
}
