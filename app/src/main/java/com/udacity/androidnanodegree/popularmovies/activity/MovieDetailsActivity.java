/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.Result;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This the MovieDetailsActivity class is show the detail's about the movies on which user is clicked
 */
public class MovieDetailsActivity extends AppCompatActivity {
    // --Commented out by Inspection (6/22/18, 3:30 PM):private final String TAG = MovieDetailsActivity.class.getSimpleName();
    @BindView(R.id.movie_poster_iv)
    ImageView mPosterImageView;
    @BindView(R.id.movie_banner_iv)
    ImageView mBannerImageView;
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
    
    private ActionBar mActionBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
         mActionBar = getSupportActionBar();
        if (mActionBar != null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            finish();
        }
        Bundle bundle = getIntent().getExtras();
        //Call setValuesToViews function to show the data send by previous activity.
        setValuesToViews(bundle);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void setValuesToViews(Bundle bundle){
        if (bundle != null) {
            Result result =  bundle.getParcelable(getString(R.string.poster_image_transition));
            if (result != null) {
                //Check every value against null before setting to views
                if (result.getPosterPath() !=null){
                    Picasso.with(this).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.POSTER_IMAGE_SIZE).concat(result.getPosterPath())).
                            placeholder(R.drawable.ic_place_holder).into(mPosterImageView);
                }
            
                if (result.getBackdropPath()!=null){
                    Picasso.with(this).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.BANNER_IMAGE_SIZE).concat(result.getBackdropPath())).
                            placeholder(R.drawable.ic_photo_black_24dp).into(mBannerImageView);
                }
                if (result.getOriginalTitle() != null){
                    mMovieTitleTextView.setText(result.getOriginalTitle());
                }
            
                if (mActionBar != null) {
                    mActionBar.setTitle(result.getOriginalTitle());
                }
                if (result.getReleaseDate() != null){
                    mMovieReleaseDateTextView.setText(result.getReleaseDate());
                }
                mVoteCountTextView.setText(String.format("Total Vote (%s)", String.valueOf(result.getVoteCount())));
                if (result.getOverview() != null){
                    mOverViewTextView.setText(result.getOverview());
                }
            
                mRatingTextView.setText(String.valueOf(result.getVoteAverage()));
                mPopularityTextView.setText(String.valueOf(new DecimalFormat("##.##").format(result.getPopularity())));
            
            
            
            
            }else{
                Toast.makeText(this, R.string.result_null_message, Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            Toast.makeText(this, R.string.result_null_message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
