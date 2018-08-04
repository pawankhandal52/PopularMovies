/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This viewpager to set the tabs with its content on home screen
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    
    //List for fragments
    private final List<Fragment> mFragmentsList = new ArrayList<>();
    //List of titles
    private final List<String> mStringList = new ArrayList<>();
    
    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }
    
    @Override
    public int getCount() {
        return mFragmentsList.size();
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringList.get(position);
    }
    
    /**
     * Add Fragment from parent activity in ViewPager
     * @param fragment Object
     * @param title String
     */
    public void addFragment(Fragment fragment, String title){
        mFragmentsList.add(fragment);
        mStringList.add(title);
    }
}
