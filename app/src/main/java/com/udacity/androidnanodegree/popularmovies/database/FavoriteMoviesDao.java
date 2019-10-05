/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This is interface is used to perform CURD on a Sql lite using Room
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
    
    
// --Commented out by Inspection START (8/2/18, 5:19 PM):
//    //Update the movie
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateFavoriteMovie(FavoriteMoviesEntity favoriteMoviesEntity);
// --Commented out by Inspection STOP (8/2/18, 5:19 PM)

// --Commented out by Inspection START (8/2/18, 5:19 PM):
//    //Delete
//    @Delete
//    void deleteFavoriteMovie(FavoriteMoviesEntity favoriteMoviesEntity);
// --Commented out by Inspection STOP (8/2/18, 5:19 PM)
    
    @Query("DELETE FROM favorite WHERE m_id = :m_id")
    void deleteFavMovieById(int m_id);
    
}
