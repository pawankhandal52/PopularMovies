/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;
import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;

import java.util.List;

/**
 * ViewModel class to prevent  calling database operation on Config Changes.
 */
public class FavoriteMoviesViewModel extends AndroidViewModel {
    
    private final String TAG = FavoriteMoviesViewModel.class.getSimpleName();
    private LiveData<List<FavoriteMoviesEntity>> mFavoriteMoviesListLiveData;
    
    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        PopularMovieDatabase popularMovieDatabase = PopularMovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "FavoriteMoviesViewModel: Actively retrieving the movies from the DataBase");
        mFavoriteMoviesListLiveData = popularMovieDatabase.favoriteMoviesDao().getAllFavoritesMovies();
    }
    
    public LiveData<List<FavoriteMoviesEntity>> getFavoriteMoviesListLiveData() {
        return mFavoriteMoviesListLiveData;
    }
}
