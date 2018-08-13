/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.networking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.androidnanodegree.popularmovies.BuildConfig;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.reviews.MovieReviews;
import com.udacity.androidnanodegree.popularmovies.models.trailers.MovieTrailers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used to do all the data operation throughout the application, This small part of AAC
 */
public class MovieRepository {
    //MovieRepository  instance
    private static MovieRepository mMovieRepository;
    
    private final String TAG = MovieRepository.class.getSimpleName();
    
    /**
     * This method is used to get the instance of this class
     *
     * @return instance of this class
     */
    public static MovieRepository getInstance() {
        if (mMovieRepository == null) {
            mMovieRepository = new MovieRepository();
        }
        return mMovieRepository;
    }
    
    /**
     * This is used to get the Movies response from TMDB Server
     *
     * @param isPopular   popular or top rated
     * @param currentPage int value of page
     * @return Response of retrofit
     */
    public LiveData<Response<Movies>> getMoviesFromTMDBServer(boolean isPopular, int currentPage) {
        final MutableLiveData<Response<Movies>> moviesResponse = new MutableLiveData<>();
        Call<Movies> moviesCall;
        if (isPopular) {
            moviesCall = callPopularMoviesApi(currentPage);
        } else {
            moviesCall = callTopRatedMoviesApi(currentPage);
        }
        
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                moviesResponse.setValue(response);
            }
            
            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t.getCause());
            }
        });
        return moviesResponse;
    }
    
    /**
     * This method is used to fetch movies review from server
     *
     * @param movieId     movie id String
     * @param currentPage page no
     * @return Response of retrofit
     */
    public LiveData<Response<MovieReviews>> getMoviesReviewFromTMDBServer(String movieId, int currentPage) {
        final MutableLiveData<Response<MovieReviews>> movieReviewsResponse = new MutableLiveData<>();
        Call<MovieReviews> reviewsCall = callMoviesReview(movieId, currentPage);
        
        reviewsCall.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviews> call, @NonNull Response<MovieReviews> response) {
                Log.d(TAG, "onResponse: getMoviesReviewFromTMDBServer ");
                movieReviewsResponse.setValue(response);
            }
            
            @Override
            public void onFailure(@NonNull Call<MovieReviews> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: getMoviesReviewFromTMDBServer", t.getCause());
            }
        });
        return movieReviewsResponse;
    }
    
    /**
     * This is used to get the Movies response from TMDB Server
     *
     * @param movieId Movie id of Movie
     * @return Response of retrofit
     */
    public LiveData<Response<MovieTrailers>> getMoviesTrailersFromTMDBServer(String movieId) {
        final MutableLiveData<Response<MovieTrailers>> movieTrailersResponse = new MutableLiveData<>();
        Call<MovieTrailers> reviewsCall = callMovieTrailers(movieId);
        
        reviewsCall.enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailers> call, @NonNull Response<MovieTrailers> response) {
                Log.d(TAG, "onResponse: getMoviesTrailersFromTMDBServer");
                movieTrailersResponse.setValue(response);
            }
            
            @Override
            public void onFailure(@NonNull Call<MovieTrailers> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: getMoviesTrailersFromTMDBServer ", t.getCause());
            }
        });
        return movieTrailersResponse;
    }
    
    private Call<Movies> callPopularMoviesApi(int currentPage) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getPopularMovies(BuildConfig.ApiKey, currentPage);
    }
    
    private Call<Movies> callTopRatedMoviesApi(int currentPage) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getTopRatedMovies(BuildConfig.ApiKey, currentPage);
    }
    
    private Call<MovieReviews> callMoviesReview(String movieId, int currentPage) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getMovieReviews(String.valueOf(movieId), BuildConfig.ApiKey, currentPage);
    }
    
    private Call<MovieTrailers> callMovieTrailers(String movieId) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getMovieTrailers(String.valueOf(movieId), BuildConfig.ApiKey);
    }
}
