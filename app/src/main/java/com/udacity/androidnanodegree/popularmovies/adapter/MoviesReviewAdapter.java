/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.models.reviews.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter class is used to provide the reviews to recycler view on Review Screen
 */
public class MoviesReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    
    private Context mContext;
    private List<Result> mReviewResults;
    private boolean isLoading =false;
    private final int VIEW_LOADING_ITEM = 0;
    private final int VIEW_ITEM = 1;
    private Animation mAnimation;
    private final String TAG = MoviesReviewAdapter.class.getSimpleName();
    private ReviewItemClickListner mReviewItemClickListner;
    public MoviesReviewAdapter(Context context ,ReviewItemClickListner reviewItemClickListner){
        this.mContext = context;
        this.mReviewItemClickListner  = reviewItemClickListner;
        mReviewResults = new ArrayList<>();
        mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.blinking_animation);
    }
    
    
    public interface ReviewItemClickListner{
        void onItemClickListener(Result result);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType){
            case VIEW_ITEM:
                viewHolder = new ReviewsHolder(inflater.inflate(R.layout.movie_review_item,parent,false));
                break;
            case VIEW_LOADING_ITEM:
                viewHolder = new LoadingReviewHolder(inflater.inflate(R.layout.loading_review_item,parent,false));
                break;
        }
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewsHolder){
            final ReviewsHolder reviewsHolder = (ReviewsHolder) holder;
            Result result = mReviewResults.get(position);
            if (result.getAuthor()!=null){
                reviewsHolder.mAuthorTextView.setText(result.getAuthor());
            }
            if (result.getContent()!=null){
                reviewsHolder.mReviewTextView.setText(result.getContent());
            }
            
            reviewsHolder.mViewMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        reviewsHolder.mReviewTextView.setMaxLines(Integer.MAX_VALUE);
                        /*reviewsHolder.mViewMoreButton.setText(mContext.getString(R.string.view_less));*/
                        reviewsHolder.mViewMoreButton.setVisibility(View.GONE);
                }
            });
        }else if(holder instanceof LoadingReviewHolder){
            LoadingReviewHolder loadingReviewHolder = (LoadingReviewHolder)holder;
            loadingReviewHolder.mLoadingReviewCardView.setAnimation(mAnimation);
        }
    }
    
    @Override
    public int getItemCount() {
        return mReviewResults == null?0:mReviewResults.size();
    }
    
    private Result getItem(int position){
        return mReviewResults.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return (position == mReviewResults.size()-1 && isLoading)?VIEW_LOADING_ITEM:VIEW_ITEM;
    }
    
    /**
     * this method is add result object in collection of result sets
     * @param result object of result
     */
    private void addReview(Result result){
        mReviewResults.add(result);
        notifyItemInserted(mReviewResults.size()-1);
    }
    
    /**
     * This methods is used to get the list of result form activity class
     * @param results Result object get form API
     */
    public void addAllReviews(List<Result> results){
        for (Result result:results) {
            addReview(result);
        }
    }
    
    /**
     * This method is used to add the loading item at the end of Recycler view
     */
    public void addLoadingItem(){
        isLoading = true;
        addReview(new Result());
    }
    
    /**
     * This method is used to remove loading item when data is downloaded form api and reday to show.
     */
    public void removeLoadingItem(){
        isLoading = false;
    
        int position = mReviewResults.size() - 1;
        Result result = getItem(position);
    
        if (result != null) {
            mReviewResults.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * This method is used to clear all the list item form List<Result>
     */
    public void removeAllItems() {
        mReviewResults.clear();
        notifyDataSetChanged();
    }
    
    class ReviewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.review_author_tv)
        TextView mAuthorTextView;
        @BindView(R.id.review_tv)
        TextView mReviewTextView;
        @BindView(R.id.view_more_button)
        Button mViewMoreButton;
        ReviewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
    
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mReviewItemClickListner.onItemClickListener(mReviewResults.get(position));
        }
    }
    
    class LoadingReviewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.loading_review_cv)
        CardView mLoadingReviewCardView;
        public LoadingReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    

    
}
