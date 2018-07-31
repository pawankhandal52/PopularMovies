/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.udacity.androidnanodegree.popularmovies.adapter.FavoriteMoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesAdapter;
import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;
import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;
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
public class HomeActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener,FavoriteMoviesAdapter.FavMovieItemClickListner {
    
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PAGE_FIRST = 1;
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
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mTotalPages = 0;
    private int currentPage = PAGE_FIRST;
    
    private MoviesAdapter mMoviesAdapter;
    private FavoriteMoviesAdapter mFavoriteMoviesAdapter;
    private boolean isPopular = true;
    private boolean isFavMoviesSelected = false;
    private NetworkReceiver mNetworkReceiver;
    
    
    //Database instance to get fav movies of user
    private PopularMovieDatabase mPopularMovieDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        
        //Set the adapter and layout manager
        mMoviesAdapter = new MoviesAdapter(this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_column_count));
        
        //init the recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMoviesAdapter);
    
        //set the adapter
        mFavoriteMoviesAdapter  = new FavoriteMoviesAdapter(this,this);
        
    
        //init the recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
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
                        if (isFavMoviesSelected){
                            mFavoriteMoviesAdapter.removeAllItems();
                            mMoviesTypeTextView.setText(R.string.favorite_movie);
                            getUserFavMovieFromDatabase();
                        }else{
                            mMoviesAdapter.removeAllItems();
    
                            currentPage = PAGE_FIRST;
                            loadMovies(isPopular);
                        }
                        
                    }
                }
        );
        
        mMoviesApi = ConfigApi.getRetrofit().create(MoviesApi.class);
        
        //This receiver and called every time when network is changed
       
            mNetworkReceiver = new NetworkReceiver(new NetworkReceiver.NetworkListener() {
                @Override
                public void connectionAvailable() {
                        /*showData();
                        if (isFavMoviesSelected){
                            mMoviesTypeTextView.setText(R.string.favorite_movie);
                            getUserFavMovieFromDatabase();
                        }else{
                            loadMovies(isPopular);
                        }*/
                        
                    
                    
                }
        
                @Override
                public void connectionUnAvailable() {
                    /*if (mRecyclerView.getChildCount() == 0) {
                        showErrorPage();
                    }*/
            
                }
            });
    
        showData();
        
        if (isFavMoviesSelected){
            mMoviesTypeTextView.setText(R.string.favorite_movie);
            getUserFavMovieFromDatabase();
        }else{
            loadMovies(isPopular);
        }
        
        mPopularMovieDatabase = PopularMovieDatabase.getInstance(getApplicationContext());
    }
    
    
    
    @Override
    protected void onStart() {
        super.onStart();
        //Register network receiver
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkReceiver);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refreshMovieList();
                return true;
            
            case R.id.action_popular:
                isPopular = true;
                isFavMoviesSelected =false;
                mMoviesTypeTextView.setText(R.string.popular_movies);
                sortMovieList();
                return true;
            
            case R.id.action_top_rated:
                isPopular = false;
                isFavMoviesSelected =false;
                mMoviesTypeTextView.setText(R.string.top_rated);
                sortMovieList();
                return true;
            case R.id.action_get_fav_movies:
                isFavMoviesSelected =true;
                mMoviesTypeTextView.setText(R.string.favorite_movie);
                getUserFavMovieFromDatabase();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void getUserFavMovieFromDatabase() {
        
        mRecyclerView.setAdapter(mFavoriteMoviesAdapter);
        
         LiveData<List<FavoriteMoviesEntity>> favoriteMoviesEntitiesLiveData= mPopularMovieDatabase.favoriteMoviesDao()
                .getAllFavoritesMovies();
    
        favoriteMoviesEntitiesLiveData.observe(this, new Observer<List<FavoriteMoviesEntity>>() {
             @Override
             public void onChanged(@Nullable List<FavoriteMoviesEntity> favoriteMoviesEntities) {
                 
                 if (favoriteMoviesEntities != null) {
                     Log.e(TAG, "getUserFavMovieFromDatabase: size"+favoriteMoviesEntities.size() );
                     mFavoriteMoviesAdapter.removeAllItems();
                     if (favoriteMoviesEntities.size()==0){
                         Log.e(TAG, "onChanged: 1" );
                         mNoInternetView.setBackgroundResource(R.mipmap.ic_launcher_round);
                         mErrorDescTextView.setText(R.string.no_fav_movies);
                         showErrorPage();
                     }else{
                         Log.e(TAG, "onChanged: 2" );
                         showData();
                         mFavoriteMoviesAdapter.addAllFavMovie(favoriteMoviesEntities);
                     }
                 }
             }
         });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            
            
            }
        });
        
        
    }
    
    private void sortMovieList() {
        mMoviesAdapter.removeAllItems();
        mRecyclerView.setAdapter(mMoviesAdapter);
        currentPage = PAGE_FIRST;
        loadMovies(isPopular);
    }
    
    private void refreshMovieList() {
        if (isFavMoviesSelected){
            mFavoriteMoviesAdapter.removeAllItems();
            mMoviesTypeTextView.setText(R.string.favorite_movie);
            getUserFavMovieFromDatabase();
        }else{
            mMoviesAdapter.removeAllItems();
            currentPage = PAGE_FIRST;
            loadMovies(isPopular);
        }
        
    }
    
    private void loadMovies(boolean isPopular) {
        Call<Movies> moviesCall;
        mProgressBar.setVisibility(View.VISIBLE);
        
        if (isPopular) {
            moviesCall = callPopularMoviesApi();
        } else {
            moviesCall = callTopRatedMoviesApi();
        }
        
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                //Check first for response is successful or not
                mSwipeRefreshLayout.setRefreshing(false);
                //Log.e(TAG, "loadMovies: "+response.raw().request().url() );
                if (response.isSuccessful()) {
                    
                     List<Result> results = getResultFromResponse(response);
                    mTotalPages = getTotalPages(response);
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
                    
                    switch (response.code()) {
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
            
            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                mErrorConstraintLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
    
    private void loadMoreMovies(boolean isPopular) {
        Call<Movies> moviesCall;
        if (isPopular) {
            moviesCall = callPopularMoviesApi();
        } else {
            moviesCall = callTopRatedMoviesApi();
        }
        
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                mSwipeRefreshLayout.setRefreshing(false);
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
                Toast.makeText(HomeActivity.this, getString(R.string.seems_you_lose_the_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private Call<Movies> callPopularMoviesApi() {
        return mMoviesApi.getPopularMovies(BuildConfig.ApiKey, currentPage);
    }
    
    private Call<Movies> callTopRatedMoviesApi() {
        return mMoviesApi.getTopRatedMovies(BuildConfig.ApiKey, currentPage);
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
    
    @Override
    public void onMovieItemClick(int clickItemIndex, Result result, View view) {
       
        Intent intent = new Intent(HomeActivity.this, MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.movie_bundle_key), result);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, getString(R.string.poster_image_transition));
        startActivity(intent, options.toBundle());
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
    public void onItemClick(FavoriteMoviesEntity favoriteMoviesEntity, View view) {
        Intent intent = new Intent(HomeActivity.this, MovieDetailsActivity.class);
       // Log.e(TAG, "onItemClick: Fav" );
        //intent.putExtra(getString(R.string.movie_bundle_key), );
        intent.putExtra(getString(R.string.fav_movie_key),favoriteMoviesEntity.getMovieId());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, getString(R.string.poster_image_transition));
        startActivity(intent, options.toBundle());
    }
    
    
}
