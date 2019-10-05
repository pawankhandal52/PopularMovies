/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.ViewModels.FavoriteMoviesViewModel;
import com.udacity.androidnanodegree.popularmovies.activity.MovieDetailsActivity;
import com.udacity.androidnanodegree.popularmovies.adapter.FavoriteMoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This fragment shows the user favorite movies
 */
public class FavoriteMovieFragment extends Fragment implements FavoriteMoviesAdapter.FavMovieItemClickListner {
    
    private static final String TAG = FavoriteMovieFragment.class.getSimpleName();
    @BindView(R.id.movie_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.error_rl)
    ConstraintLayout mErrorConstraintLayout;
    @BindView(R.id.wifi_off_iv)
    ImageView mNoInternetView;
    @BindView(R.id.error_desc_tv)
    TextView mErrorDescTextView;
    @BindView(R.id.swipe_to_refresh_tv)
    TextView mSwipeTextView;
    private Unbinder mUnbinder;
    private FavoriteMoviesAdapter mFavoriteMoviesAdapter;
    
    //Database instance to get fav movies of user
    private Context mContext;
    
    private  Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    private GridLayoutManager mGridLayoutManager;
    
    public FavoriteMovieFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        
         mGridLayoutManager = new GridLayoutManager(mContext,
                getResources().getInteger(R.integer.grid_column_count));
        
        //set the adapter
        mFavoriteMoviesAdapter = new FavoriteMoviesAdapter(mContext, this);
        
        //init the recycler view
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFavoriteMoviesAdapter);
        
        
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        
                        mFavoriteMoviesAdapter.removeAllItems();
                        getUserFavMovieFromDatabase();
                        
                    }
                }
        );
        getUserFavMovieFromDatabase();
        return view;
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
        //This used to store the state of recycler view
        mBundleRecyclerViewState = new Bundle();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(getResources().getString(R.string.recycler_scroll_position_key), mListState);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //When orientation is changed then grid column count is also changed so get every time
        int columns = getResources().getInteger(R.integer.grid_column_count);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(getResources().getString(R.string.recycler_scroll_position_key));
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                    
                }
            }, 50);
        }
        mGridLayoutManager.setSpanCount(columns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }
    @Override
    public void onItemClick(FavoriteMoviesEntity favoriteMoviesEntity, View view) {
        Intent intent = new Intent(mContext, MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.fav_movie_key), favoriteMoviesEntity.getMovieId());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, getString(R.string.poster_image_transition));
        startActivity(intent, options.toBundle());
    }
    
    private void getUserFavMovieFromDatabase() {
        
        mProgressBar.setVisibility(View.GONE);
        
        FavoriteMoviesViewModel favoriteMoviesViewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
        favoriteMoviesViewModel.getFavoriteMoviesListLiveData().observe(this, new Observer<List<FavoriteMoviesEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMoviesEntity> favoriteMoviesEntities) {
                Log.d(TAG, "Updating data from view model");
                if (favoriteMoviesEntities != null) {
                    mFavoriteMoviesAdapter.removeAllItems();
                    if (favoriteMoviesEntities.size() == 0) {
                        mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                        mErrorDescTextView.setText(R.string.no_fav_movies);
                        showErrorPage();
                    } else {
                        showData();
                        mFavoriteMoviesAdapter.addAllFavMovie(favoriteMoviesEntities);
                    }
                }
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
    
    private void showErrorPage() {
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.VISIBLE);
    }
    
    private void showData() {
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.GONE);
    }
}
