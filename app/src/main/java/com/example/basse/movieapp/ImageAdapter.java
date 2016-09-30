package com.example.basse.movieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by basse on 31-Jul-16.
 */
public class ImageAdapter extends BaseAdapter {
    Context context ;
    List<ImageView> posters;
    boolean favorite ;

    public ImageAdapter(Context context, List<ImageView> posters, boolean favorite) {
        this.context = context;
        this.posters = posters;
        this.favorite = favorite;
    }

    @Override
    public int getCount() {
        return posters.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView poster ;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            poster = new ImageView(context);
            poster.setImageResource(R.color.white);
        }
        else
            poster = (ImageView)convertView;

        if (posters.size()>1 || favorite)
        {
            poster = posters.get(position);
        }

        return poster;
    }
}
