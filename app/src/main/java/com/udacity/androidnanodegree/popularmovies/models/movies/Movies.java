/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.models.movies;

import android.databinding.ObservableList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.udacity.androidnanodegree.popularmovies.models.base.MoviesBaseDataModel;

/**
 * This  class is used to parse the Api JSON data.
 */
public class Movies implements MoviesBaseDataModel{
    
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private ObservableList<Result> results = null;
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
//    public List<Result> getResults() {
//        return results;
//    }
    
    public ObservableList<Result> getResults() {
        return results;
    }
    
    public void setResults(ObservableList<Result> results) {
        this.results = results;
    }
    
}
