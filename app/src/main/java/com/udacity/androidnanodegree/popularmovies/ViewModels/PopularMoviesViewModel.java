/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.udacity.androidnanodegree.popularmovies.BuildConfig;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.models.base.MovieBaseActivityModel;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;
import com.udacity.androidnanodegree.popularmovies.networking.ConfigApi;
import com.udacity.androidnanodegree.popularmovies.networking.MoviesApi;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Movies View model class to save the state of screen and prevent to recall network operation
 */
public class PopularMoviesViewModel extends MovieBaseActivityModel<Movies> {
    private MutableLiveData<Response<Movies>> mMoviesResponseLiveData = new MutableLiveData<>();
    private Context mContext;
    public static boolean isPopular;
    public static int currentPage;
    
    public PopularMoviesViewModel(Context context, boolean isPopular, int currentPage) {
        mContext = context;
        if (mMoviesResponseLiveData != null) {
            return;
        }
        loadMoviesFromNetwork(isPopular, currentPage);
    }

    public void loadMoviesFromNetwork(boolean isPopular, int currentPage) {
        this.isPopular = isPopular;
        this.currentPage = currentPage;
//        final MutableLiveData<Response<Movies>> moviesResponse = new MutableLiveData<>();
        Call<Movies> moviesCall;
        if (isPopular) {
            moviesCall = callPopularMoviesApi(currentPage);
        } else {
            moviesCall = callTopRatedMoviesApi(currentPage);
        }
        moviesCall.enqueue(this);
        
//        mMoviesResponseLiveData = MovieRepository.getInstance().getMoviesFromTMDBServer(isPopular, currentPage);
    }
    
    private Call<Movies> callPopularMoviesApi(int currentPage) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getPopularMovies(BuildConfig.ApiKey, currentPage);
    }
    
    private Call<Movies> callTopRatedMoviesApi(int currentPage) {
        return ConfigApi.getRetrofit().create(MoviesApi.class).getTopRatedMovies(BuildConfig.ApiKey, currentPage);
    }
    
    public LiveData<Response<Movies>> getMoviesResponseLiveData() {
        return mMoviesResponseLiveData;
    }
    
    @BindingAdapter(value = {"popularMoviesList"}, requireAll = false)
    public static void addAdapter(RecyclerView recyclerView, ObservableList<Result> popularMoviesList) {
        GridLayoutManager mGridLayoutManager;
        if(recyclerView.getAdapter() == null) {
            //Set the adapter and layout manager
            MoviesAdapter mMoviesAdapter = new MoviesAdapter(recyclerView.getContext(), new MoviesAdapter.MovieItemClickListener() {
                @Override
                public void onMovieItemClick(int clickItemIndex, Result result, View view) {
        
                }
            });
            mGridLayoutManager = new GridLayoutManager(recyclerView.getContext(),
                    recyclerView.getContext().getResources().getInteger(R.integer.grid_column_count));
    
            //init the recycler view
            recyclerView.setLayoutManager(mGridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mMoviesAdapter);
        }
        ((MoviesAdapter) recyclerView.getAdapter()).updateList(popularMoviesList);
    }
    
    public ObservableList<Result> getPopularMoviesList() {
        return getData() != null && getData().getResults() != null ? getData().getResults() : null;
    }
    
    @Override
    public void onResponse(Call call, Response response) {
        mMoviesResponseLiveData.setValue(response);
    }
}
