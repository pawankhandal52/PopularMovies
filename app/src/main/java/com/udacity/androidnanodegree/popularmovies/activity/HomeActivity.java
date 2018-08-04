/*
 * Copyright (C) 2018 The Android Popular Movies  Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.adapter.HomeViewPagerAdapter;
import com.udacity.androidnanodegree.popularmovies.fragments.FavoriteMovieFragment;
import com.udacity.androidnanodegree.popularmovies.fragments.PopularMoviesFragment;
import com.udacity.androidnanodegree.popularmovies.fragments.TopRatedFragment;
import com.udacity.androidnanodegree.popularmovies.utills.Utills;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is home activity which shows popular movies, Top Rated Movie and User favorites movie in Child fragment
 */
public class HomeActivity extends AppCompatActivity {
    //Ui Views
    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.home_view_pager)
    ViewPager mViewPager;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        
        //Set the view page and tabs
        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabIcon();
        
        //Call setValuesToViews function to show the data send by previous activity.
        //setValuesToViews(bundle);
        mViewPager.setOffscreenPageLimit(3);
        
        if (!Utills.isConnectionAvailable(this)) {
            mViewPager.setCurrentItem(3);
        }
    }
    
    //Setup view pager with tabs fragment
    private void setUpViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragment(new PopularMoviesFragment(), getString(R.string.popular));
        homeViewPagerAdapter.addFragment(new TopRatedFragment(), getString(R.string.top_rated));
        homeViewPagerAdapter.addFragment(new FavoriteMovieFragment(), getString(R.string.favorite));
        viewPager.setAdapter(homeViewPagerAdapter);
    }
    
    //This is used to set the icon and text of tabs
    private void setTabIcon() {
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(R.drawable.ic_thumb_up_black_24dp);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(R.drawable.ic_star_black_24dp);
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setIcon(R.drawable.ic_favorite_red_32dp);
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setText(getString(R.string.popular));
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setText(getString(R.string.top_rated));
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setText(getString(R.string.favorite));
    }
}
