package com.udacity.androidnanodegree.popularmovies.models.base;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.androidnanodegree.popularmovies.activity.MoviesBaseActivity.removeProgressDialog;

/**
 * Created by stemdot on 8/12/18,32
 */
public class MovieBaseActivityModel<T extends MoviesBaseDataModel> extends MovieBaseViewModel<T> implements Callback {
    
    private static final String TAG = MovieBaseActivityModel.class.getName();
    private int contentViewVisibility = View.VISIBLE;
    private int errorViewVisibility = View.GONE;
    private String errorTitle;
    private String errorMessage;
    
    @Bindable
    public int getContentViewVisibility() {
        return contentViewVisibility;
    }
    
    public void setContentViewVisibility(int contentViewVisibility) {
        this.contentViewVisibility = contentViewVisibility;
    }
    
    @Bindable
    public int getErrorViewVisibility() {
        return errorViewVisibility;
    }
    
    public void setErrorViewVisibility(int errorViewVisibility) {
        this.errorViewVisibility = errorViewVisibility;
    }
    
    @Bindable
    public String getErrorTitle() {
        return errorTitle;
    }
    
    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }
    
    @Bindable
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public void onResponse(Call call, Response response) {
    
    }
    
    @Override
    public void onFailure(Call call, Throwable t) {
        removeProgressDialog();
        if (t != null && t.getCause() != null) {
            Log.e(TAG, "onFailure: ", t.getCause());
            setContentViewVisibility(View.GONE);
            setErrorViewVisibility(View.VISIBLE);
            setErrorTitle("Something Went Wrong. Try Again!!!");
            setErrorMessage("Error Message");
        }
    }
}
