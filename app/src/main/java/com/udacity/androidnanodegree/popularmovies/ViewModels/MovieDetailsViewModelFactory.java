/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;

/**
 * This the view Model Factory use to pass parameter from activity or fragment
 */
public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private  PopularMovieDatabase mPopularMovieDatabase;
    private  int movieId;
    
    public MovieDetailsViewModelFactory(PopularMovieDatabase popularMovieDatabase, int movieId) {
        mPopularMovieDatabase = popularMovieDatabase;
        this.movieId = movieId;
    }
    
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mPopularMovieDatabase,movieId);
    }
}
