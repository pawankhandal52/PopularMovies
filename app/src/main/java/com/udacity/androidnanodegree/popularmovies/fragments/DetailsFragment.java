/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.ViewModels.MovieDetailsViewModel;
import com.udacity.androidnanodegree.popularmovies.ViewModels.MovieDetailsViewModelFactory;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;
import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass which is used to show the details of a movie when user navigate to the Tab.
 */
public class DetailsFragment extends Fragment {
    //Unbinder for to unbind views
    private Unbinder mUnbinder;
    @BindView(R.id.movie_poster_iv)
    ImageView mPosterImageView;
    
    @BindView(R.id.movie_title_tv)
    TextView mMovieTitleTextView;
    @BindView(R.id.movie_release_date_tv)
    TextView mMovieReleaseDateTextView;
    @BindView(R.id.rating_tv)
    TextView mRatingTextView;
    @BindView(R.id.vote_count_tv)
    TextView mVoteCountTextView;
    @BindView(R.id.overview_value_tv)
    TextView mOverViewTextView;
    @BindView(R.id.popularity_tv)
    TextView mPopularityTextView;
    
    private Bundle mBundle;
    private final String TAG = DetailsFragment.class.getSimpleName();
    private Context mContext;
    
    private PopularMovieDatabase mPopularMovieDatabase;
    public DetailsFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_details, container, false);
        mUnbinder = ButterKnife.bind(this,view);
        mContext = getActivity();
        mPopularMovieDatabase = PopularMovieDatabase.getInstance(mContext);
        setValuesToViews(mBundle);
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
    
    private void setValuesToViews(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(mContext.getResources().getString(R.string.movie_bundle_key))){
                
                Result result = bundle.getParcelable(mContext.getResources().getString(R.string.movie_bundle_key));
                setValuesFromMovieResult(result);
            }else if (bundle.containsKey(mContext.getResources().getString(R.string.fav_movie_key))){
                int  movieId = bundle.getInt(mContext.getResources().getString(R.string.fav_movie_key));
                //Query all the data from view model get all data from view model
                MovieDetailsViewModelFactory movieDetailsViewModelFactory = new MovieDetailsViewModelFactory(mPopularMovieDatabase,movieId);
                MovieDetailsViewModel movieDetailsViewModel = ViewModelProviders.of(this,movieDetailsViewModelFactory).get(MovieDetailsViewModel.class);
                movieDetailsViewModel.getFavoriteMoviesEntityLiveData().observe(this, new Observer<FavoriteMoviesEntity>() {
                    @Override
                    public void onChanged(@Nullable FavoriteMoviesEntity favoriteMoviesEntity) {
                        setValuesFromFavMovies(favoriteMoviesEntity);
                    }
                });
                
            }
            
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.result_null_message), Toast.LENGTH_SHORT).show();
            
        }
    }
    
    
    private void setValuesFromFavMovies(FavoriteMoviesEntity favoriteMoviesEntity) {
        if (favoriteMoviesEntity != null) {
            //Check every value against null before setting to views
            if (favoriteMoviesEntity.getPosterPath() != null) {
                Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.POSTER_IMAGE_SIZE).concat(favoriteMoviesEntity.getPosterPath())).
                        placeholder(R.drawable.ic_place_holder).into(mPosterImageView);
            }
        
        
            if (favoriteMoviesEntity.getOriginalTitle() != null) {
                mMovieTitleTextView.setText(favoriteMoviesEntity.getOriginalTitle());
            }
        
        
            if (favoriteMoviesEntity.getReleaseDate() != null) {
                mMovieReleaseDateTextView.setText(favoriteMoviesEntity.getReleaseDate());
            }
            if (favoriteMoviesEntity.getVoteCount()!= null){
                mVoteCountTextView.setText(String.format(getString(R.string.total_vote), String.valueOf(favoriteMoviesEntity.getVoteCount())));
            
            }
            if (favoriteMoviesEntity.getOverview() != null) {
                mOverViewTextView.setText(favoriteMoviesEntity.getOverview());
            }
        
            if (favoriteMoviesEntity.getVoteAverage()!=null){
                mRatingTextView.setText(String.valueOf(favoriteMoviesEntity.getVoteAverage()));
            }
            if (favoriteMoviesEntity.getPopularity()!=null){
                mPopularityTextView.setText(String.valueOf(new DecimalFormat("##.##").format(favoriteMoviesEntity.getPopularity())));
            
            }
        
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.result_null_message), Toast.LENGTH_SHORT).show();
        
        }
    }
    
    private void setValuesFromMovieResult(Result result) {
        
        if (result != null) {
            //Check every value against null before setting to views
            if (result.getPosterPath() != null) {
                Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.POSTER_IMAGE_SIZE).concat(result.getPosterPath())).
                        placeholder(R.drawable.ic_place_holder).into(mPosterImageView);
            }
        
        
            if (result.getOriginalTitle() != null) {
                mMovieTitleTextView.setText(result.getOriginalTitle());
            }
        
        
            if (result.getReleaseDate() != null) {
                mMovieReleaseDateTextView.setText(result.getReleaseDate());
            }
            if (result.getVoteCount()!= null){
                mVoteCountTextView.setText(String.format(getString(R.string.total_vote), String.valueOf(result.getVoteCount())));
            
            }
            if (result.getOverview() != null) {
                mOverViewTextView.setText(result.getOverview());
            }
        
            if (result.getVoteAverage()!=null){
                mRatingTextView.setText(String.valueOf(result.getVoteAverage()));
            }
            if (result.getPopularity()!=null){
                mPopularityTextView.setText(String.valueOf(new DecimalFormat("##.##").format(result.getPopularity())));
            
            }
        
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.result_null_message), Toast.LENGTH_SHORT).show();
        
        }
    }
}
