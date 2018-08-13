/*
 * Copyright (C) 2018 The Android Popular Movies Project made under Udacity Nanodegree Course
 * Author Pawan Kumar Sharma
 * All Rights Reserved
 */
package com.udacity.androidnanodegree.popularmovies.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.androidnanodegree.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This the SplashActivity class is show the launcher screen of popular movie app
 */
public class SplashActivity extends AppCompatActivity {
    
    @BindView(R.id.splash_icon_iv)
    ImageView mSplashIconImageView;
    @BindView(R.id.app_name_tv)
    TextView mAppNameTextView;
    @BindView(R.id.extra_tv)
    TextView mExtraTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setAnimation();
        startHomeActivity();
    }
    
    //This Function  call to show the next activity
    private void startHomeActivity() {
        final long SPLASH_DELAYS_TIME_IN_MS = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                Intent intent = new Intent(SplashActivity.this, PopularMoviesBindingActivity.class);
                //This below line is used to remove splash activity from back stack start new Stack.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, SPLASH_DELAYS_TIME_IN_MS);
    }
    
    /**
     * to set the animation on a title and subtitle
     */
    private void setAnimation() {
        final long TEXT_ANIMATION_TIME_DURATION = 1000;
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mExtraTextView, getString(R.string.scaleX), 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(TEXT_ANIMATION_TIME_DURATION);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mExtraTextView, getString(R.string.scaleY), 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(TEXT_ANIMATION_TIME_DURATION);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mExtraTextView, getString(R.string.alpha), 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(TEXT_ANIMATION_TIME_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(TEXT_ANIMATION_TIME_DURATION);
        animatorSet.start();
        
        //This for the App Icon Animation
        mSplashIconImageView.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        mSplashIconImageView.startAnimation(anim);
        
    }
}
