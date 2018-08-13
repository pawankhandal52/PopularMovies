package com.udacity.androidnanodegree.popularmovies.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.databinding.ActivityMovieBaseBindingLayoutBinding;
import com.udacity.androidnanodegree.popularmovies.models.base.MovieBaseActivityModel;
import com.udacity.androidnanodegree.popularmovies.models.base.MoviesBaseDataModel;

/**
 * Created by stemdot on 8/12/18,31
 */
public abstract class MoviesBaseActivity<T extends ViewDataBinding, V extends MovieBaseActivityModel> extends AppCompatActivity {
    protected ActivityMovieBaseBindingLayoutBinding mBaseBinding;
    protected static ProgressDialog mProgressDialog;
    private T mDataBinding;
    private V mViewModal;
    private String TAG = MoviesBaseActivity.this.getClass().getName();
    
    public void showErrorLayout() {
        removeProgressDialog();
        mViewModal.setContentViewVisibility(View.GONE);
        mViewModal.setErrorViewVisibility(View.VISIBLE);
        mViewModal.setErrorTitle("Something Went Wrong. Try Again!!!");
        mViewModal.setErrorMessage("Error Message");
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindLayout(getLayoutId());
    }
    
    protected void bindLayout(int layoutId) {
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_base_binding_layout);
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutId, mBaseBinding.contentLayout, true);
        mViewModal = getViewModalIntialize();
        onContentViewBinded(mDataBinding, mViewModal);
        mBaseBinding.setBaseVar(mViewModal);
    }
    
    public V getViewModal() {
        return mViewModal;
    }
    
    public T getActivityBinding() {
        return mDataBinding;
    }
    
    public abstract void onContentViewBinded(T dataBinding, V viewModal);
    
    public abstract V getViewModalIntialize();
    
    public abstract int getLayoutId();
    
    protected void updateData(MoviesBaseDataModel data) {
        mViewModal.setData(data);
    }
    
    public static void showProgressDialog(Context context, String message) {
        if (context == null)
            return;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new ProgressDialog(context);
        
        try {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }
    
    public static void removeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    public void onErrorBackButton(View view) {
        onBackPressed();
    }
}
