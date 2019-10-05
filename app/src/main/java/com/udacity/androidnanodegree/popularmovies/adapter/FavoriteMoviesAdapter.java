/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter is used to show the fav movies from database
 */
public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder> {
    //private final String TAG = FavoriteMoviesAdapter.class.getSimpleName();
    private List<FavoriteMoviesEntity> mFavoriteMoviesEntities;
    private Context mContext;
    private FavMovieItemClickListner mFavMovieItemClickListner;
    
    public FavoriteMoviesAdapter(Context context, FavMovieItemClickListner favMovieItemClickListner) {
        this.mContext = context;
        mFavoriteMoviesEntities = new ArrayList<>();
        this.mFavMovieItemClickListner = favMovieItemClickListner;
    }
    
    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.movie_item, parent, false);
        return new FavoriteMoviesViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(FavoriteMoviesViewHolder holder, int position) {
        FavoriteMoviesEntity favoriteMoviesEntity = mFavoriteMoviesEntities.get(position);
        if (favoriteMoviesEntity.getPosterPath() != null) {
            //Log.d(TAG, "onBindViewHolder: Fav Movies"+favoriteMoviesEntity.getTitle());
            Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL
                    .concat(AppConstants.POSTER_IMAGE_SIZE).concat(favoriteMoviesEntity.getPosterPath())).
                    placeholder(R.drawable.ic_place_holder).into(holder.mImageView);
        }
    }
    
    @Override
    public int getItemCount() {
        if (mFavoriteMoviesEntities == null) {
            return 0;
        }
        return mFavoriteMoviesEntities.size();
    }
    
    /**
     * This method is used to add the Trailer's result in a list
     *
     * @param favoriteMoviesEntity its object of result
     */
    private void addFavMovie(FavoriteMoviesEntity favoriteMoviesEntity) {
        mFavoriteMoviesEntities.add(favoriteMoviesEntity);
        notifyItemInserted(mFavoriteMoviesEntities.size() - 1);
    }
    
    /**
     * This method is used to add one bye one object in list form activity.
     *
     * @param favoriteMoviesEntities list of results object's
     */
    public void addAllFavMovie(List<FavoriteMoviesEntity> favoriteMoviesEntities) {
        for (FavoriteMoviesEntity favoriteMoviesEntity : favoriteMoviesEntities) {
            addFavMovie(favoriteMoviesEntity);
        }
    }
    
    /**
     * To Remove all the items from List
     */
    public void removeAllItems() {
        mFavoriteMoviesEntities.clear();
        notifyDataSetChanged();
    }
    
    /**
     * To implement on Click listner on a item in Recycler view.
     */
    public interface FavMovieItemClickListner {
        void onItemClick(FavoriteMoviesEntity favoriteMoviesEntity, View view);
    }
    
    /**
     * ViewHolder class for Movie item in a Adapter
     */
    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_poster_iv)
        ImageView mImageView;
        
        FavoriteMoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            mFavMovieItemClickListner.onItemClick(mFavoriteMoviesEntities.get(getAdapterPosition()), mImageView);
        }
    }
}
