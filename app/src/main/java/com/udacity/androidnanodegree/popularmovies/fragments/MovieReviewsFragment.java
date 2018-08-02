/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.fragments;

/*
 * This fragment is used to show the Movie Reviews selected by the users
 */

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.udacity.androidnanodegree.popularmovies.ViewModels.MovieReviewsViewModel;
import com.udacity.androidnanodegree.popularmovies.ViewModels.MovieReviewsViewModelFactory;
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesReviewAdapter;
import com.udacity.androidnanodegree.popularmovies.models.reviews.MovieReviews;
import com.udacity.androidnanodegree.popularmovies.models.reviews.Result;
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

public class MovieReviewsFragment extends Fragment implements MoviesReviewAdapter.ReviewItemClickListner{
    
    //Unbinder to unbind views when fragment is detach
    private Unbinder mUnbinder;
    private static final int PAGE_FIRST = 1;
    private final String TAG = MovieReviewsFragment.class.getSimpleName();
    
    private MoviesReviewAdapter mMoviesReviewAdapter;
    private Context mContext;
    
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mTotalPages = 0;
    private int currentPage = PAGE_FIRST;
    private MoviesApi mMoviesApi;
    private Integer mMovieId;
    private NetworkReceiver mNetworkReceiver;
    private Bundle mBundle;
    
    //Ui Items from layout
    @BindView(R.id.reviews_rv)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.reviews_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.reviews_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.review_error_cl)
    ConstraintLayout mErrorConstraintLayout;
    @BindView(R.id.wifi_off_iv)
    ImageView mNoInternetView;
    @BindView(R.id.error_desc_tv)
    TextView mErrorDescTextView;
    @BindView(R.id.swipe_to_refresh_tv)
    TextView mSwipeTextView;
    public MovieReviewsFragment() {
        
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
       Log.e(TAG, "onCreate: ");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        mUnbinder = ButterKnife.bind(this,view);
        
        
        //Get the Movie id from bundle pass by the activity
        mContext = getActivity();
    
        //Log.e(TAG, "onCreateView: " );
        //Check bundle have a which key
        if (mBundle.containsKey(mContext.getResources().getString(R.string.movie_bundle_key))){
            com.udacity.androidnanodegree.popularmovies.models.movies.Result result = mBundle.getParcelable(mContext.getString(R.string.movie_bundle_key));
            if (result != null) {
                mMovieId = result.getId();
            }else{
                Toast.makeText(mContext,getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }else if (mBundle.containsKey(mContext.getResources().getString(R.string.fav_movie_key))){
            mMovieId = mBundle.getInt(mContext.getResources().getString(R.string.fav_movie_key));
           
        }else{
            Toast.makeText(mContext,getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        
        //Set the adapter and Recycler view
        mMoviesReviewAdapter = new MoviesReviewAdapter(getActivity(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewsRecyclerView.setAdapter(mMoviesReviewAdapter);
    
        //Log.e(TAG, "onCreateView: " );
        //Add lisner fo on scroll listner
        mReviewsRecyclerView.addOnScrollListener(new PagingListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage +=1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e(TAG, "run: reviewSize" );
                        loadMoreMoviesReviews();
                        
                    }
                },1000);
            }
    
            @Override
            protected int getTotalPageCount() {
                return mTotalPages;
            }
    
            @Override
            protected boolean isLastPage() {
                return isLastPage;
            }
    
            @Override
            protected boolean isLoading() {
                return isLoading;
            }
        });
    
        //Initialize movie api
        mMoviesApi = ConfigApi.getRetrofit().create(MoviesApi.class);
    
       
        
        //Swipe to refresh layour
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMoviesReviewAdapter.removeAllItems();
                currentPage = PAGE_FIRST;
                showData();
                loadMovieReviews();
            }
        });
    
        mNetworkReceiver = new NetworkReceiver(new NetworkReceiver.NetworkListener() {
            @Override
            public void connectionAvailable() {
                   showData();
                   loadMovieReviews();
               
            }
        
            @Override
            public void connectionUnAvailable() {
                if (mReviewsRecyclerView.getChildCount() == 0) {
                    showErrorPage(R.drawable.ic_signal_wifi_off_red_24dp,getString(R.string.seems_you_lose_the_internet_connection));
    
                }
            }
        });
    
    
        mContext.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return view;
    }
    
  
    private void loadMovieReviews(){
        mProgressBar.setVisibility(View.VISIBLE);
        MovieReviewsViewModelFactory movieReviewsViewModelFactory = new MovieReviewsViewModelFactory(String.valueOf(mMovieId),currentPage);
        MovieReviewsViewModel movieReviewsViewModel = ViewModelProviders.of(this,movieReviewsViewModelFactory).get(MovieReviewsViewModel.class);
        movieReviewsViewModel.getMovieReviewsResponseLiveData().observe(this, new Observer<Response<MovieReviews>>() {
            @Override
            public void onChanged(@Nullable Response<MovieReviews> movieReviewsResponse) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (movieReviewsResponse == null){
                    showErrorPage(R.drawable.ic_signal_wifi_off_red_24dp,getString(R.string.seems_you_lose_the_internet_connection));
                    return;
                }
                if (movieReviewsResponse.isSuccessful()){
                    List<Result> results = getResultFromMovieReviewResponse(movieReviewsResponse);
                    if (results != null){
                        if (results.size() != 0){
                            showData();
                            mTotalPages = getTotalPages(movieReviewsResponse);
                            mMoviesReviewAdapter.addAllReviews(results);
                            if (currentPage < mTotalPages) {
                                mMoviesReviewAdapter.addLoadingItem();
                                // Log.e(TAG, "onResponse: "+currentPage +", total" );
                            } else {
                                isLastPage = true;
                            }
                        }else{
                            showErrorPage(R.drawable.ic_stars_black_24dp,
                                    getString(R.string.no_reviews));
                        }
            
                    }else{
                        showErrorPage(R.mipmap.ic_launcher_round,
                                getString(R.string.something_went_wrong));
                    }
        
        
                }else{
                    switch (movieReviewsResponse.code()) {
                        case 401:
                            Log.e(TAG, " Invalid API key: You must be granted a valid key.");
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.error_401));
                            break;
                        case 404:
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.error_404));
                            Log.e(TAG, " The resource you requested could not be found.");
                            break;
                        default:
                            Log.e(TAG, " Unknown Error Try Again!!");
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.unknown_error));
                            break;
            
                    }
                }
            }
        });
    }

    
/*    private void loadMovieReviews(){
        mProgressBar.setVisibility(View.VISIBLE);
        callMoviesReview().enqueue(new Callback<MovieReviews>(){
    
            @Override
            public void onResponse(@NonNull Call<MovieReviews> call, @NonNull Response<MovieReviews> response) {
                //First remove the loading footer
               // Log.e(TAG, "onResponse: "+response.raw().request().url() );
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    List<Result> results = getResultFromMovieReviewResponse(response);
                    if (results != null){
                        if (results.size() != 0){
                            showData();
                            mTotalPages = getTotalPages(response);
                            mMoviesReviewAdapter.addAllReviews(results);
                            if (currentPage < mTotalPages) {
                                mMoviesReviewAdapter.addLoadingItem();
                               // Log.e(TAG, "onResponse: "+currentPage +", total" );
                            } else {
                                isLastPage = true;
                            }
                        }else{
                            showErrorPage(R.drawable.ic_stars_black_24dp,
                                    getString(R.string.no_reviews));
                        }
                        
                    }else{
                        showErrorPage(R.mipmap.ic_launcher_round,
                                getString(R.string.something_went_wrong));
                    }
                    
                    
                }else{
                    switch (response.code()) {
                        case 401:
                            Log.e(TAG, " Invalid API key: You must be granted a valid key.");
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.error_401));
                            break;
                        case 404:
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.error_404));
                            Log.e(TAG, " The resource you requested could not be found.");
                            break;
                        default:
                            Log.e(TAG, " Unknown Error Try Again!!");
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.unknown_error));
                            break;
        
                    }
                }
            }
    
            @Override
            public void onFailure(@NonNull Call<MovieReviews> call, @NonNull Throwable t) {
                //Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(mContext, getString(R.string.seems_you_lose_the_internet_connection), Toast.LENGTH_SHORT).show();
                showErrorPage(R.drawable.ic_signal_wifi_off_red_24dp,getString(R.string.seems_you_lose_the_internet_connection));
            }
        });
    }*/
    
    private void loadMoreMoviesReviews(){
        callMoviesReview().enqueue(new Callback<MovieReviews>(){
        
            @Override
            public void onResponse(@NonNull Call<MovieReviews> call, @NonNull Response<MovieReviews> response) {
                //First remove the loading footer
                mMoviesReviewAdapter.removeLoadingItem();
                isLoading = false;
                //Log.e(TAG, "onResponse: load more"+currentPage );
                //Log.e(TAG, "onResponse: load more"+response.raw().request().url() );
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    List<Result> results = getResultFromMovieReviewResponse(response);
                    mTotalPages = getTotalPages(response);
                    mMoviesReviewAdapter.addAllReviews(results);
                    if (currentPage < mTotalPages) {
                        mMoviesReviewAdapter.addLoadingItem();
                    } else {
                        isLastPage = true;
                    }
                
                }else{
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
            public void onFailure(@NonNull Call<MovieReviews> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(mContext, getString(R.string.seems_you_lose_the_internet_connection), Toast.LENGTH_SHORT).show();
            
            }
        });
    }
    private int getTotalPages(Response<MovieReviews> movieReviewsResponse){
        MovieReviews movieReviews = movieReviewsResponse.body();
        if (movieReviews!=null){
            return movieReviews.getTotalPages();
        }
        return 0;
    }
    
    private Call<MovieReviews> callMoviesReview(){
        return ConfigApi.getRetrofit().create(MoviesApi.class).getMovieReviews(String.valueOf(mMovieId), BuildConfig.ApiKey,currentPage);
    }
    
    private List<Result>  getResultFromMovieReviewResponse(Response<MovieReviews> movieReviewsResponse){
        MovieReviews movieReviews = movieReviewsResponse.body();
        return movieReviews != null ? movieReviews.getResults() : null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        //Log.e(TAG, "onDestroy: " );
    }
    
   private void showErrorPage(int resourceId, String text){
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.VISIBLE);
        mNoInternetView.setBackgroundResource(resourceId);
        mErrorDescTextView.setText(text);
   }
   
   private void showData(){
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.GONE);
   }
    

    
   
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mNetworkReceiver);
    }
    
    @Override
    public void onItemClickListener(Result result) {
        if (result!= null){
            if (result.getUrl()!=null){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getUrl()));
                if (intent.resolveActivity(mContext.getPackageManager())!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext,getResources().getString( R.string.no_app_found_to_view), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext,getResources().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            }
            
        }else{
            Toast.makeText(mContext, getResources().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
        }
        
    }
}
