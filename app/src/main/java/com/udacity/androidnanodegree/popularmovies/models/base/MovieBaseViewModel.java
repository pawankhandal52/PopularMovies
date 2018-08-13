package com.udacity.androidnanodegree.popularmovies.models.base;

import android.databinding.BaseObservable;

/**
 * Created by stemdot on 8/12/18,35
 */
public class MovieBaseViewModel<T extends MoviesBaseDataModel> extends BaseObservable {
    private T data;
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
