/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * This is interface is used to perform CURD on a Sqlite using Room
 */
@Dao
public interface FavoriteMoviesDao {
    //Query to read all the user Favorite movies.
    @Query("SELECT * FROM favorite ORDER BY release_date")
    LiveData<List<FavoriteMoviesEntity>> getAllFavoritesMovies();
    
    @Query("SELECT * FROM favorite WHERE m_id = :m_id")
    LiveData<FavoriteMoviesEntity> getFavMovieById(int m_id);
    
    
    
    
    //Insert new Movie in a database
    @Insert
    void insertFavoriteMovie(FavoriteMoviesEntity favoriteMoviesEntity);
    
    
    //Update the movie
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(FavoriteMoviesEntity favoriteMoviesEntity);
    
    //Delete
    @Delete
    void deleteFavoriteMovie(FavoriteMoviesEntity favoriteMoviesEntity);
    
    @Query("DELETE FROM favorite WHERE m_id = :m_id")
    int deleteFavMovieById(int m_id);
    
}
