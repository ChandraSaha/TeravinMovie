package com.chandrasaha.teravintest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chandrasaha.teravintest.R;
import com.chandrasaha.teravintest.model.Movie;

import java.util.List;

/**
 * Created by Chandra Saha on 8/29/2017.
 */

public class MovieAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    List<Movie> movieList;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.movie_item, null);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        tvTitle.setText(movieList.get(position).getTitle());
        tvDate.setText(movieList.get(position).getDate());
        return convertView;
    }
}
