/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.utills;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * This Class is used to implement paging functionality on Movie list when user wants to see more result
 */
public abstract class PagingListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold;
    private  GridLayoutManager mGridLayoutManager;
    private  LinearLayoutManager mLinearLayoutManager;
    
    /**
     * Support only for Grid layout manager
     * @param gridLayoutManager layout manager object
     */
    protected PagingListener(GridLayoutManager gridLayoutManager) {
        this.mGridLayoutManager = gridLayoutManager;
        visibleThreshold = visibleThreshold * mGridLayoutManager.getSpanCount();
    }
    
    protected PagingListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }
    
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            int visibleItemCount  = mGridLayoutManager.getChildCount();
            int totalItemCount = mGridLayoutManager.getItemCount();
            int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
    
    
            if (!isLoading() && !isLastPage()) {
                if ((visibleThreshold + lastVisibleItemPosition+visibleItemCount) >= totalItemCount
                        && lastVisibleItemPosition >= 0
                        && totalItemCount <= getTotalPageCount()) {
                    loadMoreItems();
                }
            }
        }else if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            int visibleItemCount = mLinearLayoutManager.getChildCount();
            int totalItemCount = mLinearLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
    
            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= getTotalPageCount()) {
                    loadMoreItems();
                }
            }
        }
        
    }
    
    protected abstract void loadMoreItems();
    
    protected abstract int getTotalPageCount();
    
    protected abstract boolean isLastPage();
    
    protected abstract boolean isLoading();
}
