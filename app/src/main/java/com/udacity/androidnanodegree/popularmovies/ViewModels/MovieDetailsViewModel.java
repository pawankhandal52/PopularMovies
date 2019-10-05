/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;
import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;

/**
 * This is ViewModel class to get the Movie by its id
 */
public class MovieDetailsViewModel extends ViewModel {
    
    private final String TAG = MovieDetailsViewModel.class.getSimpleName();
    private LiveData<FavoriteMoviesEntity> mFavoriteMoviesEntityLiveData;
    
    public MovieDetailsViewModel(PopularMovieDatabase movieDatabase, int movieId) {
        mFavoriteMoviesEntityLiveData = movieDatabase.favoriteMoviesDao().getFavMovieById(movieId);
        Log.d(TAG, "recive movie by its id from database ");
    }
    
    public LiveData<FavoriteMoviesEntity> getFavoriteMoviesEntityLiveData() {
        return mFavoriteMoviesEntityLiveData;
    }
    
}
