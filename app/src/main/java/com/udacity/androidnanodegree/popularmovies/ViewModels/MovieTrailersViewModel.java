/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.androidnanodegree.popularmovies.models.trailers.MovieTrailers;
import com.udacity.androidnanodegree.popularmovies.networking.MovieRepository;

import retrofit2.Response;

/**
 *  View Model container to provide data of movies Trailers
 */
public class MovieTrailersViewModel extends ViewModel {
    
    
    private LiveData<Response<MovieTrailers>> mMovieTrailerResponseLiveData;
    
    public MovieTrailersViewModel(String movieId) {
        if (mMovieTrailerResponseLiveData !=null){
            return;
        }
        
        loadMovieTrailerFromNetwork(movieId);
    }
    
    private void loadMovieTrailerFromNetwork(String movieId) {
        mMovieTrailerResponseLiveData = MovieRepository.getInstance().getMoviesTrailersFromTMDBServer(movieId);
    }
    
    public LiveData<Response<MovieTrailers>> getMovieTrailerResponseLiveData() {
        return mMovieTrailerResponseLiveData;
    }
}
