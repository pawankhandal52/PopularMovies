/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.androidnanodegree.popularmovies.BuildConfig;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.models.Movies;
import com.udacity.androidnanodegree.popularmovies.models.Result;
import com.udacity.androidnanodegree.popularmovies.networking.ConfigApi;
import com.udacity.androidnanodegree.popularmovies.networking.MoviesApi;
import com.udacity.androidnanodegree.popularmovies.utills.NetworkReceiver;
import com.udacity.androidnanodegree.popularmovies.utills.PagingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This the HomeActivity class is show the Popular movies / Top Rated movies in a grid view
 */
public class HomeActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener{
    
    private static final String TAG = HomeActivity.class.getSimpleName();
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
    @BindView(R.id.movies_sort_type_tv)
    TextView mMoviesTypeTextView;
    private MoviesApi mMoviesApi;
    
    
    private static final int PAGE_FIRST = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mTotalPages = 0;
    private int currentPage = PAGE_FIRST;
    
    private MoviesAdapter mMoviesAdapter;
    private boolean isPopular = true;
    
    private NetworkReceiver mNetworkReceiver;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    
        mMoviesAdapter = new MoviesAdapter(this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_column_count));
    
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMoviesAdapter);
    
        
        //Set the Movies listing type
        mMoviesTypeTextView.setText(R.string.popular_movies);
        
        //This is the scrollListener for paging /load more data
        mRecyclerView.addOnScrollListener(new PagingListener(gridLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreMovies(isPopular);
                    }
                },1000);
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
                        loadMovies(isPopular);
                    }
                }
        );
    
    
        mMoviesApi = ConfigApi.getRetrofit().create(MoviesApi.class);
    
        //This receiver and called every time when network is changed
        mNetworkReceiver = new NetworkReceiver(new NetworkReceiver.NetworkListener() {
            @Override
            public void connectionAvailable() {
                showData();
                loadMovies(isPopular);
            }
    
            @Override
            public void connectionUnAvailable() {
                if (mRecyclerView.getChildCount()==0){
                    showErrorPage();
                }
                
            }
        });
        
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mNetworkReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkReceiver);
    }
    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refreshMovieList();
                return true;
        
            case R.id.action_popular:
                isPopular = true;
                mMoviesTypeTextView.setText(R.string.popular_movies);
                sortMovieList();
                return true;
        
            case R.id.action_top_rated:
                isPopular = false;
                mMoviesTypeTextView.setText(R.string.top_rated);
                sortMovieList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void sortMovieList() {
        mMoviesAdapter.removeAllItems();
        currentPage = PAGE_FIRST;
        loadMovies(isPopular);
    }
    
    private void refreshMovieList() {
        mMoviesAdapter.removeAllItems();
        currentPage = PAGE_FIRST;
        loadMovies(isPopular);
    }
    
    
    private void loadMovies(boolean isPopular){
        Call<Movies> moviesCall;
        mProgressBar.setVisibility(View.VISIBLE);
        if (isPopular){
            moviesCall = callPopularMoviesApi();
        }else{
            moviesCall = callTopRatedMoviesApi();
        }
    
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                //Check first for response is successful or not
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    
                    List<Result> results = getResultFromResponse(response);
                    if (results == null){
                        mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                        mErrorDescTextView.setText(R.string.something_went_wrong);
                        showErrorPage();
                    }else{
                        showData();
                    }
                    mTotalPages = getTotalPages(response);
                    
                    
                    mMoviesAdapter.addAll(results);
                    if (currentPage <= mTotalPages){
                            mMoviesAdapter.addLoadingFooter();
                    }else{
                        isLastPage = true;
                    }
                }else{
                    showErrorPage();
                   
                    switch (response.code()){
                        case 401:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.error_401);
                            break;
                        case 404:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.error_404 );
                            break;
                        default:
                            mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                            mErrorDescTextView.setText(R.string.unknown_error );
                            break;
    
                    }
                }
            }
    
            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                mErrorConstraintLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
    
    private void loadMoreMovies(boolean isPopular){
        Call<Movies> moviesCall;
        if (isPopular){
            moviesCall = callPopularMoviesApi();
        }else{
            moviesCall = callTopRatedMoviesApi();
        }
        
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                //First remove the loading footer
                mMoviesAdapter.removeLoadingFooter();
                isLoading = false;
    
                
                if (response.isSuccessful()){
                    List<Result> results = getResultFromResponse(response);
                    mTotalPages = getTotalPages(response);
                    mMoviesAdapter.addAll(results);
                    if (currentPage <= mTotalPages){
                        mMoviesAdapter.addLoadingFooter();
                    }else{
                        isLastPage = true;
                    }
                }else{
                    
                    switch (response.code()){
                        case 401:
                            Log.e(TAG, " Invalid API key: You must be granted a valid key." );
                            break;
                        case 404:
                            Log.e(TAG, " The resource you requested could not be found." );
                            break;
                        default:
                            Log.e(TAG, " Unknown Error Try Again!!" );
                            break;
        
                    }
                }
            }
    
            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e(TAG, "onFailure: "+ t.getLocalizedMessage());
                Toast.makeText(HomeActivity.this,getString(R.string.seems_you_lose_the_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Call<Movies> callPopularMoviesApi(){
        return mMoviesApi.getPopularMovies(BuildConfig.ApiKey,currentPage);
    }
    
    private Call<Movies> callTopRatedMoviesApi(){
        return mMoviesApi.getTopRatedMovies(BuildConfig.ApiKey,currentPage);
    }
    
    private List<Result> getResultFromResponse(Response<Movies> moviesResponse){
            Movies movies = moviesResponse.body();
            return movies != null ? movies.getResults() : null;
    }
    
    private int getTotalPages(Response<Movies> moviesResponse){
        Movies movies = moviesResponse.body();
        if (movies != null) {
            return movies.getTotalPages();
        }
        return 0;
    }
    
    @Override
    public void onMovieItemClick(int clickItemIndex,Result result,View view) {
        Intent intent = new Intent(HomeActivity.this,MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.poster_image_transition),result);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, getString(R.string.poster_image_transition));
        startActivity(intent,options.toBundle());
    }
    
    private void showErrorPage(){
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void showData(){
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.GONE);
    }
    
    
}
