/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * This is class is used to create a sql lite table to store the user fav movies locally
 */
@Entity(tableName = "favorite")
public class FavoriteMoviesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "m_id")
    private Integer movieId;
    @ColumnInfo(name = "vote_count")
    private Integer voteCount;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private Double voteAverage;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "popularity")
    private Double popularity;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "original_language")
    private String originalLanguage;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    @ColumnInfo(name = "adult")
    private Boolean adult;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    
    public FavoriteMoviesEntity(Integer movieId, Integer voteCount, Double voteAverage, String title, Double popularity, String posterPath, String originalLanguage, String originalTitle, String backdropPath, Boolean adult, String overview, String releaseDate) {
        this.movieId = movieId;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getMovieId() {
        return movieId;
    }
    
    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
    
    public Integer getVoteCount() {
        return voteCount;
    }
    
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
    
    public Double getVoteAverage() {
        return voteAverage;
    }
    
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Double getPopularity() {
        return popularity;
    }
    
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
    
    public String getPosterPath() {
        return posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    
    public String getOriginalLanguage() {
        return originalLanguage;
    }
    
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
    
    public String getOriginalTitle() {
        return originalTitle;
    }
    
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    
    public String getBackdropPath() {
        return backdropPath;
    }
    
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    
    public Boolean getAdult() {
        return adult;
    }
    
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
