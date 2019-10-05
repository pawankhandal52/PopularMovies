/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

/**
 * This the Factory class which is used to pass the data from Activity to Registry Via View Model
 */
public class MovieTrailerViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String mMovieId;
    
    public MovieTrailerViewModelFactory(String movieId) {
        mMovieId = movieId;
    }
    
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieTrailersViewModel(mMovieId);
    }
}
