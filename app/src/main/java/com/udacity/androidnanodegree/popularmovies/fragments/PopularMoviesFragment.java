package com.udacity.androidnanodegree.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.androidnanodegree.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {
    
    public PopularMoviesFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false);
    }
    
}
