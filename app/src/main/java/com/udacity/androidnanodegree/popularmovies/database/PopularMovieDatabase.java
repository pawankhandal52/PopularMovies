/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * This is single ton class to create a database using room database
 */
@Database(entities = {FavoriteMoviesEntity.class},version = 2,exportSchema = false)
public abstract class PopularMovieDatabase extends RoomDatabase {
    private final static String TAG = PopularMovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static PopularMovieDatabase sInstance;
    
    public static PopularMovieDatabase getInstance(Context context) {
        //TODO Please remove allow main thread line in production
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PopularMovieDatabase.class, PopularMovieDatabase.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance ");
        return sInstance;
    }
    
    public abstract FavoriteMoviesDao favoriteMoviesDao();
}
    

