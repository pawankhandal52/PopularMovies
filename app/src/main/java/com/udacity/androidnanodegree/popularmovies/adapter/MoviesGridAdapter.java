package com.udacity.androidnanodegree.popularmovies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.androidnanodegree.popularmovies.R;
import com.udacity.androidnanodegree.popularmovies.constants.AppConstants;
import com.udacity.androidnanodegree.popularmovies.models.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stemdot on 6/26/18,42
 */
public class MoviesGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Result> mResultList;
    private final String TAG = MoviesGridAdapter.class.getSimpleName();
    MoviesGridItemClickListener mMoviesGridItemClickListener;
    
    public interface MoviesGridItemClickListener{
        void onItemClick(Result result,View view);
    }
    public MoviesGridAdapter(Context context,MoviesGridItemClickListener moviesGridItemClickListener){
        this.mContext = context;
        this.mMoviesGridItemClickListener = moviesGridItemClickListener;
        mResultList = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return mResultList == null?0:mResultList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mResultList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    private void add(Result result){
        mResultList.add(result);
        notifyDataSetChanged();
    }
    
    
    
    public void addAll(List<Result> results){
        for (Result result: results) {
            add(result);
        }
    }
    
    public void removeAll(){
        mResultList.clear();
        notifyDataSetChanged();
    }
    
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        final ImageView imageView;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
            gridView = new GridView(mContext);
        if (layoutInflater != null) {
            gridView = layoutInflater.inflate(R.layout.movie_item,null);
        }
        imageView = gridView.findViewById(R.id.movie_poster_iv);
            
        
    
        final Result result = (Result) getItem(position);
        Log.e(TAG, "getView: "+ result.getOriginalTitle() +",position "+position);
        if (result.getPosterPath()!=null){
            Picasso.with(mContext).load(AppConstants.IMAGE_BASE_URL
                    .concat(AppConstants.POSTER_IMAGE_SIZE).concat(result.getPosterPath())).
                    placeholder(R.drawable.ic_place_holder).into(imageView);
        }
    
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoviesGridItemClickListener.onItemClick(result,imageView);
            }
        });
        return gridView;
    }
}
