/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.networking.MovieRepository;

import retrofit2.Response;

/**
 * Movies View model class to save the state of screen and prevent to recall network operation
 */
public class MoviesViewModel extends ViewModel {
    private LiveData<Response<Movies>> mMoviesResponseLiveData;
    
    MoviesViewModel(boolean isPopular,int currentPage){
        if (mMoviesResponseLiveData != null){
            return;
        }
        loadMoviesFromNetwork(isPopular,currentPage);
    }
    
    public void loadMoviesFromNetwork(boolean isPopular, int currentPage) {
        mMoviesResponseLiveData = MovieRepository.getInstance().getMoviesFromTMDBServer(isPopular,currentPage);
    }
    public LiveData<Response<Movies>> getMoviesResponseLiveData(){
        return mMoviesResponseLiveData;
    }
}
