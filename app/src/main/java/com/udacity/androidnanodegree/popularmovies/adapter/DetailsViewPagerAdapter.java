/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Android Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to add the fragment to the view pager.
 */
public class DetailsViewPagerAdapter extends FragmentPagerAdapter {
    //List for fragments
    private final List<Fragment> mFragmentsList = new ArrayList<>();
    //List of titles
    private final List<String> mStringList = new ArrayList<>();
    
    //Bundle for values
    private Bundle mBundle;
    
    public DetailsViewPagerAdapter(FragmentManager fm,Bundle bundle) {
        super(fm);
        this.mBundle = bundle;
    }
    
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentsList.get(position);
        fragment.setArguments(mBundle);
        return fragment;
    }
    
    @Override
    public int getCount() {
        return mFragmentsList.size();
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringList.get(position);
    }
    
    public void addFragment(Fragment fragment,String title){
        mFragmentsList.add(fragment);
        mStringList.add(title);
    }
}
