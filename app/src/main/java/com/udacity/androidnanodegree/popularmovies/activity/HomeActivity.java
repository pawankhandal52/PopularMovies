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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stemdot on 7/31/18,18
 */
public class HomeActivity1 extends AppCompatActivity {
    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.home_view_pager)
    ViewPager mViewPager;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_1);
        ButterKnife.bind(this);
    
    
        //Set the view page and tabs
        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabIcon();
    
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
    
    private void setUpViewPager(ViewPager viewPager){
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragment(new PopularMoviesFragment(),getString(R.string.popular));
        homeViewPagerAdapter.addFragment(new TopRatedFragment(),getString(R.string.top_rated));
        homeViewPagerAdapter.addFragment(new FavoriteMovieFragment(),getString(R.string.favorite));
        viewPager.setAdapter(homeViewPagerAdapter);
    }
    
    private void setTabIcon(){
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_thumb_up_black_24dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_star_black_24dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_red_32dp);
        mTabLayout.getTabAt(0).setText(getString(R.string.popular));
        mTabLayout.getTabAt(1).setText(getString(R.string.top_rated));
        mTabLayout.getTabAt(2).setText(getString(R.string.favorite));
    }
}
