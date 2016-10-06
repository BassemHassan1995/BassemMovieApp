package com.example.basse.movieapp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by basse on 31-Jul-16.
 */
public class ImageAdapter extends BaseAdapter {
    Context context;
    List<String> posters_paths;
    boolean favorite;

    public ImageAdapter(Context context, List<String> posters_paths, boolean favorite) {
        this.context = context;
        this.posters_paths = posters_paths;
        this.favorite = favorite;
    }

    @Override
    public int getCount() {
        return posters_paths.size();
    }

    @Override
    public Object getItem(int position) {
        return posters_paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels / 2;

        int width = displayMetrics.widthPixels / 2;
        ImageView poster;
        String posterPath = posters_paths.get(position);


        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            poster = new ImageView(context);
            poster.setLayoutParams(new GridView.LayoutParams(width, height));
            poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            poster = (ImageView) convertView;
        }

        if (posterPath.contains("null")) {
            poster.setImageResource(R.drawable.no_poster_available);
        } else {
            Glide.with(context).load(posterPath).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
        }

        return poster;
    }
}
