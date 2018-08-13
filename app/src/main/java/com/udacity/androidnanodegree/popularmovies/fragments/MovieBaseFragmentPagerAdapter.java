package com.udacity.androidnanodegree.popularmovies.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.udacity.androidnanodegree.popularmovies.activity.MoviesBaseActivity;
import com.udacity.androidnanodegree.popularmovies.models.base.MovieBaseViewModel;
import com.udacity.androidnanodegree.popularmovies.models.base.MoviesBaseDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stemdot on 8/12/18,16
 */
public abstract class MovieBaseFragmentPagerAdapter <ITEM_T extends MoviesBaseDataModel, VIEW_MODEL_T extends MovieBaseViewModel> extends FragmentStatePagerAdapter{
    
    private MoviesBaseActivity mContext;
    private List<ITEM_T> items = new ArrayList<>();
    
    public MovieBaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    public MovieBaseFragmentPagerAdapter(List<ITEM_T> mItems, MoviesBaseActivity moviesBaseActivity, FragmentManager fm) {
        super(fm);
        mContext = moviesBaseActivity;
        items = mItems;
    }
}
