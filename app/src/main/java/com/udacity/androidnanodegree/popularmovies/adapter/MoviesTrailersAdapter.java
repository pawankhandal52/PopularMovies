/*
 * Copyright (C) 2018 The Android Popular  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.trailers.Result;
import com.udacity.androidnanodegree.popularmovies.utills.Utills;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter class is used to provide the movie trailers to recycler view on trailers Screen
 */
public class MoviesTrailersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = MoviesTrailersAdapter.class.getSimpleName();
    private List<Result> mTrailerResultList;
    private Context mContext;
    private TrailerItemClickListner mTrailerItemClickListner;
    
    public MoviesTrailersAdapter(Context context, TrailerItemClickListner trailerItemClickListner) {
        this.mContext = context;
        this.mTrailerItemClickListner = trailerItemClickListner;
        mTrailerResultList = new ArrayList<>();
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new TrailersHolder(layoutInflater.inflate(R.layout.movie_trailer_item, parent, false));
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TrailersHolder) {
            TrailersHolder trailersHolder = (TrailersHolder) holder;
            final Result result = mTrailerResultList.get(position);
                /*if (result.getName() != null){
                    trailersHolder.mTrailerTitleTextView.setText(result.getName());
                }*/
            if (result.getKey() != null) {
                Picasso.with(mContext).load(getThumbUrl(result.getKey())).
                        placeholder(R.drawable.ic_place_holder).into(trailersHolder.mTrailerThumbImageView);
            }
            trailersHolder.mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareImage(Utills.getYoutubeLink(result.getKey()));
                }
            });
            
        }
    }
    
    // Method to share any image.
    private void shareImage(String youtubeLink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this trailer " + youtubeLink);
        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
    }
    
    @Override
    public int getItemCount() {
        return mTrailerResultList != null ? mTrailerResultList.size() : 0;
    }
    
    /**
     * This method is used to add the Trailer's result in a list
     *
     * @param result its object of result
     */
    private void addTrailer(Result result) {
        mTrailerResultList.add(result);
        notifyItemInserted(mTrailerResultList.size() - 1);
    }
    
    /**
     * This method is used to add one bye one object in list form activity.
     *
     * @param results list of results object's
     */
    public void addAllTrailer(List<Result> results) {
        for (Result result : results) {
            addTrailer(result);
        }
    }
    
    private String getThumbUrl(String keyOfMovie) {
        //Log.e(TAG, "getThumbUrl: " + AppConstants.MOVIE_TRAILER_THUMB.replace("{id}", keyOfMovie));
        return AppConstants.MOVIE_TRAILER_THUMB.replace("{id}", keyOfMovie);
    }
    
    /**
     * Item Click Listner
     */
    public interface TrailerItemClickListner {
        void onItemClickListner(Result result);
    }
    
    class TrailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_thumb_iv)
        ImageView mTrailerThumbImageView;
        @BindView(R.id.trailer_title_tv)
        TextView mTrailerTitleTextView;
        @BindView(R.id.shared_ib)
        ImageButton mShareButton;
        
        TrailersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mTrailerItemClickListner.onItemClickListner(mTrailerResultList.get(position));
        }
    }
}
