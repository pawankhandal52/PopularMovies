/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.udacity.androidnanodegree.popularmovies.adapter.MoviesTrailersAdapter;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.trailers.MovieTrailers;
import com.udacity.androidnanodegree.popularmovies.models.trailers.Result;
import com.udacity.androidnanodegree.popularmovies.networking.ConfigApi;
import com.udacity.androidnanodegree.popularmovies.networking.MoviesApi;
import com.udacity.androidnanodegree.popularmovies.utills.NetworkReceiver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple subclass is used to show the trailers of movie selected by the user on movie lis screen.
 */
public class MovieTrailersFragment extends Fragment implements MoviesTrailersAdapter.TrailerItemClickListner{
    private Unbinder mUnbinder;
    private final String TAG = MovieTrailersFragment.class.getSimpleName();
    
    //Ui Elements id
    @BindView(R.id.trailer_pb)
    ProgressBar mProgressBar;
    @BindView(R.id.trailer_rv)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.trailer_swipe_to_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.trailer_error_cl)
    ConstraintLayout mErrorConstraintLayout;
    @BindView(R.id.wifi_off_iv)
    ImageView mNoInternetView;
    @BindView(R.id.error_desc_tv)
    TextView mErrorDescTextView;
    @BindView(R.id.swipe_to_refresh_tv)
    TextView mSwipeTextView;
    
    
    private Bundle mBundle;
    private Context mContext;
    private Integer mMovieId;
    private NetworkReceiver mNetworkReceiver;
    private MoviesApi mMoviesApi;
    private MoviesTrailersAdapter mMoviesTrailersAdapter;
    
    public MovieTrailersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        mUnbinder = ButterKnife.bind(this,view);
        mContext = getActivity();
    
        /*com.udacity.androidnanodegree.popularmovies.models.movies.Result result = mBundle.getParcelable(mContext.getString(R.string.movie_bundle_key));
        if (result != null) {
            mMovieId = result.getId();
        }else{
            Toast.makeText(mContext,getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }*/
    
    
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
        mMoviesTrailersAdapter = new MoviesTrailersAdapter(mContext,this);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTrailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTrailersRecyclerView.setAdapter(mMoviesTrailersAdapter);
        
        mMoviesApi = ConfigApi.getRetrofit().create(MoviesApi.class);
    
        mNetworkReceiver = new NetworkReceiver(new NetworkReceiver.NetworkListener() {
            @Override
            public void connectionAvailable() {
                //Log.e(TAG, "onCreate: "+currentPage );
                showData();
                loadMoviesTrailers();
            }
        
            @Override
            public void connectionUnAvailable() {
                if (mTrailersRecyclerView.getChildCount() == 0) {
                    showErrorPage(R.drawable.ic_signal_wifi_off_red_24dp,
                            getString(R.string.seems_you_lose_the_internet_connection));
                
                }
            }
        });
        mContext.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return view;
    }
    
    
    
    private void loadMoviesTrailers() {
        callMovieTrailers().enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailers> call, @NonNull Response<MovieTrailers> response) {
                Log.e(TAG, "onResponse: "+response.raw().request().url() );
                if (response.isSuccessful()){
                    List<Result> results = getResultFromMovieTrailerResponse(response);
                    if (results != null){
                        if (results.size() != 0){
                                showData();
                                mMoviesTrailersAdapter.addAllTrailer(results);
                        }else{
                            showErrorPage(R.mipmap.ic_launcher_round,getString(R.string.no_trailer_founds));
                        }
                    }else{
                        showErrorPage(R.mipmap.ic_launcher_round,
                                getString(R.string.something_went_wrong));
                    }
                }else{
                    switch (response.code()){
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
            public void onFailure(@NonNull Call<MovieTrailers> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage(),t );
                showErrorPage(R.drawable.ic_signal_wifi_off_red_24dp,
                        mContext.getResources().getString(R.string.seems_you_lose_the_internet_connection));
            }
        });
    }
    
    
    private Call<MovieTrailers> callMovieTrailers(){
        return mMoviesApi.getMovieTrailers(String.valueOf(mMovieId), BuildConfig.ApiKey);
    }
    
    private List<Result> getResultFromMovieTrailerResponse(Response<MovieTrailers> movieTrailersResponse){
        MovieTrailers movieTrailers = movieTrailersResponse.body();
        return movieTrailers != null ? movieTrailers.getResults() : null;
    }
    //This method is used to show the data when data is downloaded from the web and loaded in ui
    private void showData() {
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.GONE);
    }
    
    //This method is use to show the error layout when data is not laoded and some error occurs.
    private void showErrorPage(int resourceId, String text){
        mProgressBar.setVisibility(View.GONE);
        mErrorConstraintLayout.setVisibility(View.VISIBLE);
        mNoInternetView.setBackgroundResource(resourceId);
        mErrorDescTextView.setText(text);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mNetworkReceiver);
    }
    
    @Override
    public void onItemClickListner(Result result) {
        if(result != null){
            if (result.getSite().equals(AppConstants.TRAILER_SITE_TYPE)){
                
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeLink(result.getKey())));
                if (intent.resolveActivity(mContext.getPackageManager())!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, R.string.no_app_found_to_view, Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Log.e(TAG, "onItemClickListner: null object" );
            Toast.makeText(mContext, getResources().getString(R.string.result_null_message), Toast.LENGTH_SHORT).show();
        }
        
    }
    
    private String getYoutubeLink(String keyOfVideo){
        Log.e(TAG, "getYoutubeLink: "+AppConstants.YOUTUBE_LINK.replace("{key_of_video}",keyOfVideo) );
        return AppConstants.YOUTUBE_LINK.replace("{key_of_video}",keyOfVideo);
    }
}