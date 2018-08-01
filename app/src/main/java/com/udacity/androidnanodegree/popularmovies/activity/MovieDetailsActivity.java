/*
 * Copyright (C) 2018 The Android Popular Movies Stage 1 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.adapter.DetailsViewPagerAdapter;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.database.FavoriteMoviesEntity;
import com.udacity.androidnanodegree.popularmovies.database.PopularMovieDatabase;
import com.udacity.androidnanodegree.popularmovies.fragments.DetailsFragment;
import com.udacity.androidnanodegree.popularmovies.fragments.MovieReviewsFragment;
import com.udacity.androidnanodegree.popularmovies.fragments.MovieTrailersFragment;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;
import com.udacity.androidnanodegree.popularmovies.utills.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This the MovieDetailsActivity class is show the detail's about the movies on which user is clicked
 */
public class MovieDetailsActivity extends AppCompatActivity{
    private final String TAG = MovieDetailsActivity.class.getSimpleName();
    
    
    private ActionBar mActionBar;
    @BindView(R.id.details_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.details_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.movie_banner_iv)
    ImageView mBannerImageView;
    private Bundle bundle;
    //To change the menu icon
    private Menu mMenu;
    
    
    //Fav database instance
    private PopularMovieDatabase mPopularMovieDatabase;
    private Context mContext;
    private boolean isFav = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        mPopularMovieDatabase = PopularMovieDatabase.getInstance(getApplicationContext());
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            finish();
        }
    
        //Add the tabs to tab Layout
        
         bundle = getIntent().getExtras();
        setCollapsingToolBarImage(bundle);
    
        
    
        //Set the view page and tabs
        setUpViewPager(mViewPager,bundle);
        mTabLayout.setupWithViewPager(mViewPager);
        
        //Call setValuesToViews function to show the data send by previous activity.
        //setValuesToViews(bundle);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
            }
    
            @Override
            public void onPageSelected(int position) {
                //Log.e(TAG, "onPageSelected: "+position );
            }
    
            @Override
            public void onPageScrollStateChanged(int state) {
        
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
    }
    
    private void insertFavoriteMoviesInDatabase(Bundle bundle){
        if (bundle!=null){
            Result result = bundle.getParcelable(getString(R.string.movie_bundle_key));
            if (result != null) {
                //Now get all the value which you want to insert
                int movieId = 0;
                if (result.getId()!= null){
                    movieId = result.getId();
                }
                int voteCount = 0;
                if (result.getVoteCount()!=null){
                    voteCount = result.getVoteCount();
                }
                String title = null;
                if (result.getTitle() != null){
                    title = result.getTitle();
                }
                
                Double popularity = 0.00;
                if (result.getPopularity()!=null){
                    popularity = result.getPopularity();
                }
                
                String orignalLanguage = null;
                if (result.getOriginalLanguage() !=null){
                    orignalLanguage = result.getOriginalLanguage();
                }
                
                String orignalTitle = null;
                if (result.getOriginalTitle() != null){
                    orignalTitle = result.getOriginalTitle();
                }
                String poster = null;
                if (result.getPosterPath() !=null){
                    poster = result.getPosterPath();
                }
                String banner = null;
                if (result.getBackdropPath()!=null){
                    banner= result.getBackdropPath();
                }
                boolean  adult = false;
                if(result.getAdult() !=null){
                    adult = result.getAdult();
                }
                String synopsis = null;
                if (result.getOverview()!=null){
                    synopsis = result.getOverview();
                }
                Double ratings = 0.0;
                if (result.getVoteAverage()!=null){
                    ratings = result.getVoteAverage();
                }
                String releaseDate = null;
                if (result.getReleaseDate()!=null){
                    releaseDate = result.getReleaseDate();
                }
    
                final FavoriteMoviesEntity favoriteMoviesEntity = new FavoriteMoviesEntity(movieId,voteCount,ratings,title,
                        popularity,poster,orignalLanguage,orignalTitle,banner,adult,synopsis,releaseDate);
                
                //Now call dao method to insert this in a database
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mPopularMovieDatabase.favoriteMoviesDao().insertFavoriteMovie(favoriteMoviesEntity);
                    }
                });
                
                //Log.e(TAG, "insertFavoriteMoviesInDatabase: "+favoriteMoviesEntity );
                mMenu.getItem(0).setIcon(R.drawable.ic_favorite_black_32dp);
                
                
            }else{
                Toast.makeText(this, R.string.unable_to_save_your_fav_movies, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.unable_to_save_your_fav_movies, Toast.LENGTH_SHORT).show();
            
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_movie_details,menu);
        //Check movie is fav
        if (isFav){
            mMenu.getItem(0).setIcon(R.drawable.ic_favorite_black_32dp);
        }else{
            mMenu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return true;
    }
    
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_favorite:
                Log.e(TAG, "onOptionsItemSelected: "+isFav);
                if (isFav){
                    
                    removeMovieFromDatabase(bundle);
                }else{
                    insertFavoriteMoviesInDatabase(bundle);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
                
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void setUpViewPager(ViewPager viewPager, Bundle bundle){
        DetailsViewPagerAdapter detailsViewPagerAdapter = new DetailsViewPagerAdapter(getSupportFragmentManager(),bundle);
        detailsViewPagerAdapter.addFragment(new DetailsFragment(),getString(R.string.tab_title_details));
        detailsViewPagerAdapter.addFragment(new MovieTrailersFragment(),getString(R.string.tab_title_trailers));
        detailsViewPagerAdapter.addFragment(new MovieReviewsFragment(),getString(R.string.tab_title_reviews));
        viewPager.setAdapter(detailsViewPagerAdapter);
    }
    
    private void setCollapsingToolBarImage(Bundle bundle){
        
        if (bundle!=null){
            if (bundle.containsKey(getResources().getString(R.string.movie_bundle_key))){
                
                final Result result = bundle.getParcelable(getString(R.string.movie_bundle_key));
                
                //Now check if movie is fav if yes then change the icon to fav
                if (result!= null){
                    //Fetch movie by id using live data
                     LiveData<FavoriteMoviesEntity> favoriteMoviesEntity = mPopularMovieDatabase.favoriteMoviesDao().getFavMovieById(result.getId());
                   // Log.e(TAG, "run1: data is called from data base by movie id" );
                    favoriteMoviesEntity.observe(this, new Observer<FavoriteMoviesEntity>() {
                        @Override
                        public void onChanged(@Nullable FavoriteMoviesEntity favoriteMoviesEntity) {
                            if (favoriteMoviesEntity!=null){
                                isFav = true;
        
                            }
                        }
                    });
                }
                if (result != null && result.getBackdropPath() != null) {
                    Picasso.with(this).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.BANNER_IMAGE_SIZE).concat(result.getBackdropPath())).
                            placeholder(R.drawable.ic_photo_black_24dp).into(mBannerImageView);
                }
                if (mActionBar != null) {
                    if (result != null && result.getOriginalTitle()!=null) {
                        mActionBar.setTitle(result.getOriginalTitle());
                    }
                }
            }else if (bundle.containsKey(getResources().getString(R.string.fav_movie_key))){
                
                final int movieId = bundle.getInt(getResources().getString(R.string.fav_movie_key));
                isFav = true;
    
                LiveData<FavoriteMoviesEntity> favoriteMoviesEntity = mPopularMovieDatabase.favoriteMoviesDao().getFavMovieById(movieId);
                //Log.e(TAG, "run2: data is called from data base by movie id" );
                favoriteMoviesEntity.observe(this, new Observer<FavoriteMoviesEntity>() {
                    @Override
                    public void onChanged(@Nullable FavoriteMoviesEntity favoriteMoviesEntity) {
                        if (favoriteMoviesEntity != null && favoriteMoviesEntity.getBackdropPath() != null) {
                            Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL.concat(AppConstants.BANNER_IMAGE_SIZE).concat(favoriteMoviesEntity.getBackdropPath())).
                                    placeholder(R.drawable.ic_photo_black_24dp).into(mBannerImageView);
                        }
                        if (mActionBar != null) {
                            if (favoriteMoviesEntity != null && favoriteMoviesEntity.getOriginalTitle()!=null) {
                                mActionBar.setTitle(favoriteMoviesEntity.getOriginalTitle());
                            }
                        }
                    }
                });
                
                
            }
            
            
        }else {
            Toast.makeText(this, R.string.result_null_message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
   private void removeMovieFromDatabase(Bundle bundle){
        if (bundle!=null){
            //First get the movie id
            final int movieId;
            if (bundle.containsKey(getString(R.string.movie_bundle_key))){
                Result result = bundle.getParcelable(getString(R.string.movie_bundle_key));
                if (result != null) {
                    movieId = result.getId();
                }else {
                    return;
                }
            }else{
                 movieId =  bundle.getInt(getResources().getString(R.string.fav_movie_key));
                
            }
    
    
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    int b = mPopularMovieDatabase.favoriteMoviesDao().deleteFavMovieById(movieId);
                    Log.e(TAG, "run: "+b +"movie id "+movieId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isFav = false;
                            mMenu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
                            finish();
                        }
                    });
            
                }
            });
        }else{
            Toast.makeText(this, getString(R.string.unable_to_unfav), Toast.LENGTH_SHORT).show();
        }
   }
    
}
