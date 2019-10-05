/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the Adapter class which is used to show the Movies in Grid View using Recycler view
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROGRESS_ITEM = 1;
    private final Context mContext;
    private final List<Result> mResults;
    private final Animation mAnimation;
    final private MovieItemClickListener mMovieItemClickListener;
    // --Commented out by Inspection (8/2/18, 5:10 PM):private final String TAG = MoviesAdapter.class.getSimpleName();
    private boolean isLoading = false;
    
    public MoviesAdapter(Context context, MovieItemClickListener movieItemClickListener) {
        this.mContext = context;
        mResults = new ArrayList<>();
        this.mMovieItemClickListener = movieItemClickListener;
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.blinking_animation);
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_ITEM:
                viewHolder = new MoviesViewHolder(mInflater.inflate(R.layout.movie_item, parent, false));
                break;
            case VIEW_PROGRESS_ITEM:
                viewHolder = new LoadingViewHolder(mInflater.inflate(R.layout.movie_placeholder_layout, parent, false));
                break;
        }
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoviesViewHolder) {
            MoviesViewHolder moviesViewHolder = (MoviesViewHolder) holder;
            Result result = mResults.get(position);
            if (result.getPosterPath() != null) {
                Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL
                        .concat(AppConstants.POSTER_IMAGE_SIZE).concat(result.getPosterPath())).
                        placeholder(R.drawable.ic_place_holder).into(moviesViewHolder.mImageView);
            }
            
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.mCardView.startAnimation(mAnimation);
        }
        
    }
    
    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }
    
    @Override
    public int getItemViewType(int position) {
        return (position == mResults.size() - 1 && isLoading) ? VIEW_PROGRESS_ITEM : VIEW_ITEM;
    }
    
    /**
     * This method is used to add the Result object in a list which is show on the home screen.
     *
     * @param result this is the object which is add in a list
     */
    private void add(Result result) {
        mResults.add(result);
        notifyItemInserted(mResults.size() - 1);
    }
    
    /**
     * This method is used to pass the result list from Activity
     *
     * @param mResultList List of result object
     */
    public void addAll(List<Result> mResultList) {
        for (Result result : mResultList) {
            add(result);
        }
    }
    
    /**
     * This method is used to add the loading footer at the bottom of recycler view when data is loading from api
     */
    public void addLoadingFooter() {
        isLoading = true;
        add(new Result());
    }
    
    /**
     * This method is used to remove the loading when data is downloaded from api
     */
    public void removeLoadingFooter() {
        isLoading = false;
        
        int position = mResults.size() - 1;
        Result result = getItem(position);
        
        if (result != null) {
            mResults.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * This method is used to clear all the list item form List<Result>
     */
    public void removeAllItems() {
        mResults.clear();
        notifyDataSetChanged();
    }
    
    private Result getItem(int position) {
        return mResults.get(position);
    }
    
    /**
     * This interface is used to implement the click functionality on a recycler view item
     */
    public interface MovieItemClickListener {
        void onMovieItemClick(int clickItemIndex, Result result, View view);
    }
    
    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_poster_iv)
        ImageView mImageView;
        
        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mMovieItemClickListener.onMovieItemClick(clickedPosition, mResults.get(clickedPosition), mImageView);
        }
    }
    
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeholder_cv)
        CardView mCardView;
        
        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            
        }
    }
    
}
