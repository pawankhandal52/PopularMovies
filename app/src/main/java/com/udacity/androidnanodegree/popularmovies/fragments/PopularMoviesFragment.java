/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.udacity.androidnanodegree.popularmovies.BuildConfig;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.ViewModels.MoviesViewModel;
import com.udacity.androidnanodegree.popularmovies.ViewModels.MoviesViewModelFactory;
import com.udacity.androidnanodegree.popularmovies.activity.MovieDetailsActivity;
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;
import com.udacity.androidnanodegree.popularmovies.networking.ConfigApi;
import com.udacity.androidnanodegree.popularmovies.networking.MoviesApi;
import com.udacity.androidnanodegree.popularmovies.utills.NetworkReceiver;
import com.udacity.androidnanodegree.popularmovies.utills.PagingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment shows Popular movies
 */
public class PopularMoviesFragment extends Fragment implements MoviesAdapter.MovieItemClickListener {
    private static final int PAGE_FIRST = 1;
    private final String TAG = PopularMoviesFragment.class.getSimpleName();
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
    private MoviesApi mMoviesApi;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mTotalPages = 0;
    private int currentPage = PAGE_FIRST;
    
    private MoviesAdapter mMoviesAdapter;
    private NetworkReceiver mNetworkReceiver;
    private Context mContext;
    private  Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    private GridLayoutManager mGridLayoutManager;
    
    
    public PopularMoviesFragment() {
        // Required empty public constructor
    }
   
    
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
    
        //int posterWidth = 500; // size in pixels (just a random size). You may use other values.
        
       
        //Set the adapter and layout manager
        mMoviesAdapter = new MoviesAdapter(mContext, this);
         mGridLayoutManager = new GridLayoutManager(mContext,
                 getResources().getInteger(R.integer.grid_column_count));
        
        //init the recycler view
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMoviesAdapter);
        //This is the scrollListener for paging /load more data
        mRecyclerView.addOnScrollListener(new PagingListener(mGridLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreMovies();
                    }
                }, 1000);
            }
            
            @Override
            public int getTotalPageCount() {
                return mTotalPages;
            }
            
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        
        
        
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
                        
                        mMoviesAdapter.removeAllItems();
                        
                        currentPage = PAGE_FIRST;
                        loadMovies();
                        
                    }
                }
        );
        
        mMoviesApi = ConfigApi.getRetrofit().create(MoviesApi.class);
        
        //This receiver and called every time when network is changed
        
        mNetworkReceiver = new NetworkReceiver(new NetworkReceiver.NetworkListener() {
            @Override
            public void connectionAvailable() {
                showData();
                loadMovies();
                
            }
            
            @Override
            public void connectionUnAvailable() {
                if (mRecyclerView.getChildCount() == 0) {
                    showErrorPage();
                }
                
            }
        });
        
        mContext.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mNetworkReceiver);
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
    
    @Override
    public void onMovieItemClick(int clickItemIndex, Result result, View view) {
        Intent intent = new Intent(mContext, MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.movie_bundle_key), result);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, getString(R.string.poster_image_transition));
        startActivity(intent, options.toBundle());
    }
    
    private void loadMovies() {
        mProgressBar.setVisibility(View.VISIBLE);
        //Check for the null value
        MoviesViewModelFactory moviesViewModelFactory = new MoviesViewModelFactory(true, currentPage);
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this, moviesViewModelFactory).get(MoviesViewModel.class);
        moviesViewModel.getMoviesResponseLiveData().observe(this, new Observer<Response<Movies>>() {
            @Override
            public void onChanged(@Nullable Response<Movies> moviesResponse) {
                mSwipeRefreshLayout.setRefreshing(false);
                //Log.e(TAG, "onChanged: New Data" );
                if (moviesResponse == null) {
                    mErrorConstraintLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                if (moviesResponse.isSuccessful()) {
                    
                    List<Result> results = getResultFromResponse(moviesResponse);
                    mTotalPages = getTotalPages(moviesResponse);
                    if (results == null) {
                        mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                        mErrorDescTextView.setText(R.string.something_went_wrong);
                        showErrorPage();
                    } else {
                        showData();
                    }
                    mMoviesAdapter.addAll(results);
                    if (currentPage <= mTotalPages) {
                        mMoviesAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                } else {
                    showErrorPage();
                    
                    switch (moviesResponse.code()) {
                        case 401:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.error_401);
                            break;
                        case 404:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.error_404);
                            break;
                        default:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.unknown_error);
                            break;
                        
                    }
                }
            }
        });
        
    }
    
    //This is for load more movies
    private void loadMoreMovies() {
        Call<Movies> moviesCall = callPopularMoviesApi();
        
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                //First remove the loading footer
                mMoviesAdapter.removeLoadingFooter();
                isLoading = false;
                
                if (response.isSuccessful()) {
                    List<Result> results = getResultFromResponse(response);
                    mTotalPages = getTotalPages(response);
                    mMoviesAdapter.addAll(results);
                    
                    if (currentPage <= mTotalPages) {
                        mMoviesAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                } else {
                    
                    switch (response.code()) {
                        case 401:
                            Log.e(TAG, " Invalid API key: You must be granted a valid key.");
                            break;
                        case 404:
                            Log.e(TAG, " The resource you requested could not be found.");
                            break;
                        default:
                            Log.e(TAG, " Unknown Error Try Again!!");
                            break;
                        
                    }
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(mContext, getString(R.string.seems_you_lose_the_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private Call<Movies> callPopularMoviesApi() {
        return mMoviesApi.getPopularMovies(BuildConfig.ApiKey, currentPage);
    }
    
    private List<Result> getResultFromResponse(Response<Movies> moviesResponse) {
        Movies movies = moviesResponse.body();
        return movies != null ? movies.getResults() : null;
    }
    
    private int getTotalPages(Response<Movies> moviesResponse) {
        Movies movies = moviesResponse.body();
        if (movies != null) {
            return movies.getTotalPages();
        }
        return 0;
    }
    
    /*private int calculateBestSpanCount(int posterWidth) {
        Display display = null;
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
            Objects.requireNonNull(display).getMetrics(outMetrics);
        }
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }*/
}

