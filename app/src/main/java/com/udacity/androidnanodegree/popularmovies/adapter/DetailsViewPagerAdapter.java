/*
 * Copyright (C) 2018 The Android Popular Movies Stage 2 Project made under Udacity Android Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
    private final Bundle mBundle;
    
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
    
    /**
     * Add Fragment in a view pager
     * @param fragment Fragment Object
     * @param title tab title
     */
    public void addFragment(Fragment fragment,String title){
        mFragmentsList.add(fragment);
        mStringList.add(title);
    }
}
