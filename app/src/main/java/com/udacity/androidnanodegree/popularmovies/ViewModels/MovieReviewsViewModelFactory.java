/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * This the Factory class which is used to pass the data from Activity to Registry Via View Model
 */
public class MovieReviewsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String mMovieId;
    private int mCurrentPage;
    
    public MovieReviewsViewModelFactory(String movieId, int currentPage) {
        mMovieId = movieId;
        mCurrentPage = currentPage;
    }
    
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieReviewsViewModel(mMovieId,mCurrentPage);
    }
}
