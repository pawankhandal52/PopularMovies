/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.utills;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


/**
 * This Class is used to implement paging functionality on Movie list when user wants to see more result
 */
public abstract class PagingListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold;
    private final GridLayoutManager mGridLayoutManager;
    
    /**
     * Support only for Grid layout manager
     * @param gridLayoutManager layout manager object
     */
    protected PagingListener(GridLayoutManager gridLayoutManager) {
        this.mGridLayoutManager = gridLayoutManager;
        visibleThreshold = visibleThreshold * mGridLayoutManager.getSpanCount();
    }
    
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount  = mGridLayoutManager.getChildCount();
        int totalItemCount = mGridLayoutManager.getItemCount();
        int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
    
        
        if (!isLoading() && !isLastPage()) {
            if ((visibleThreshold + lastVisibleItemPosition+visibleItemCount) >= totalItemCount
                    && lastVisibleItemPosition >= 0
                    && totalItemCount <= getTotalPageCount()) {
                loadMoreItems();
                Log.d("TAG", "onScrolled: called");
            }
        }
    }
    
    protected abstract void loadMoreItems();
    
    protected abstract int getTotalPageCount();
    
    protected abstract boolean isLastPage();
    
    protected abstract boolean isLoading();
}