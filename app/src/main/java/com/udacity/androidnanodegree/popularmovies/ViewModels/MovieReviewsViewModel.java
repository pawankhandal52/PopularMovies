/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.androidnanodegree.popularmovies.models.reviews.MovieReviews;
import com.udacity.androidnanodegree.popularmovies.networking.MovieRepository;

import retrofit2.Response;

/**
 *  View Model container to provide data of movies reviews
 */
public class MovieReviewsViewModel extends ViewModel {
    private  LiveData<Response<MovieReviews>> mMovieReviewsResponseLiveData;
    
    public MovieReviewsViewModel(String movieId ,int currentPage) {
        if (mMovieReviewsResponseLiveData != null){
            return;
        }
        loadMovieReviewsFromNetwork(movieId,currentPage);
    }
    
    private void loadMovieReviewsFromNetwork(String movieId,int currentPage) {
        mMovieReviewsResponseLiveData = MovieRepository.getInstance().getMoviesReviewFromTMDBServer(movieId,currentPage);
    }
    
    public LiveData<Response<MovieReviews>> getMovieReviewsResponseLiveData() {
        return mMovieReviewsResponseLiveData;
    }
}
