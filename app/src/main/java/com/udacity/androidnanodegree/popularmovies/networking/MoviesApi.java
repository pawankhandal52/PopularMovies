/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.networking;

import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.reviews.MovieReviews;
import com.udacity.androidnanodegree.popularmovies.models.trailers.MovieTrailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface have all the api which is requested in this app using retrofit
 */
public interface MoviesApi {
    //This abstract method is used to when we want to fetch the Popular movies from api
    @GET(AppConstants.POPULAR_MOVIES)
    Call<Movies> getPopularMovies(
            @Query(AppConstants.API_KEY) String apiKey,
            @Query(AppConstants.PAGE_KEY) int pageNo);
    
    //This abstract method is used to get the top rated movies from api
    @GET(AppConstants.TOP_RATED_MOVIES)
    Call<Movies> getTopRatedMovies(
            @Query(AppConstants.API_KEY) String apiKey,
            @Query(AppConstants.PAGE_KEY) int pageNo
    );
    //this abstract method is used to when get the trailers of movies
    @GET(AppConstants.MOVIES_TRAILERS)
    Call<MovieTrailers> getMovieTrailers(
            @Path(value="id",encoded = true)String movieId,
            @Query(AppConstants.API_KEY) String apiKey
            
    );
    
    //this abstract method is used to get the reviews of movie.
    @GET(AppConstants.MOVIE_REVIEWS)
    Call<MovieReviews> getMovieReviews(
            @Path(value="id",encoded = true)String movieId,
            @Query(AppConstants.API_KEY) String apiKey,
            @Query(AppConstants.PAGE_KEY) int pageNo
    );
}
