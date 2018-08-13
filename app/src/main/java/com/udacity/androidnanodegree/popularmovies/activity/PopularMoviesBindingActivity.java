package com.udacity.androidnanodegree.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.ViewModels.PopularMoviesViewModel;
import com.udacity.androidnanodegree.popularmovies.databinding.PopularMoviesBindingLayoutBinding;
import com.udacity.androidnanodegree.popularmovies.models.movies.Movies;
import com.udacity.androidnanodegree.popularmovies.models.movies.Result;

import java.util.List;

import retrofit2.Response;

import static com.udacity.androidnanodegree.popularmovies.ViewModels.PopularMoviesViewModel.currentPage;

/**
 * Created by stemdot on 8/12/18,30
 */
public class PopularMoviesBindingActivity extends MoviesBaseActivity<PopularMoviesBindingLayoutBinding, PopularMoviesViewModel>{
    private PopularMoviesViewModel popularMoviesViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PopularMoviesViewModel popularMoviesViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);
        
        popularMoviesViewModel.getMoviesResponseLiveData().observe(this, new Observer<Response<Movies>>() {
            @Override
            public void onChanged(@Nullable Response<Movies> moviesResponse) {
                if (moviesResponse == null) {
                    showErrorLayout();
                    return;
                }
                if (moviesResponse.isSuccessful()) {
                
                    List<Result> results = getResultFromResponse(moviesResponse);
                    if (results == null) {
                        showErrorLayout();
                        return;
                    }
//                    else {
//                        showData();
//                    }
                } else {
                    showErrorLayout();
                    return;
                }
            }
        });
    }
    
    private List<Result> getResultFromResponse(Response<Movies> moviesResponse) {
        Movies movies = moviesResponse.body();
        return movies != null ? movies.getResults() : null;
    }
    
    @Override
    public void onContentViewBinded(PopularMoviesBindingLayoutBinding dataBinding, PopularMoviesViewModel viewModal) {
        dataBinding.setPopularMovieModel(viewModal);
    }
    
    @Override
    public PopularMoviesViewModel getViewModalIntialize() {
//        return new PopularMoviesViewModel(this);
        popularMoviesViewModel = new PopularMoviesViewModel(this, true, currentPage);
        return popularMoviesViewModel;
    }
    
    @Override
    public int getLayoutId() {
        return R.layout.popular_movies_binding_layout;
    }
}
