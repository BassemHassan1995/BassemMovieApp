package com.example.basse.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by basse on 16-Sep-16.
 */
public class FavouritesFragment extends Fragment {

    public ImageAdapter imageAdapter;
    public ArrayList<ImageView> posters = new ArrayList<ImageView>();
    public GridView gridView;
    public List<Movie> favouriteMovies = new ArrayList<>();
    MovieDbHelper helper = MainActivity.helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview_movies);
        posters.add(new ImageView(getContext()));
        imageAdapter = new ImageAdapter(getContext(), posters);
        gridView.setAdapter(imageAdapter);

        favouriteMovies = getDbMovies();
        if (favouriteMovies != null) {
            updatePosters(favouriteMovies);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("Position", position);
                intent.putExtra("Movie", favouriteMovies.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {

        favouriteMovies = getDbMovies();
        if (favouriteMovies != null) {
            updatePosters(favouriteMovies);
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_favourites, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_clear_all) {
            deleteDbMovies();
            imageAdapter.notifyDataSetChanged();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePosters(List<Movie> favouriteMovies) {
        posters.clear();
        for (Movie movie : favouriteMovies) {
            posters.add(Utility.getPoster(getContext(), movie));
        }
        imageAdapter.notifyDataSetChanged();
    }

    private List<Movie> getDbMovies() {
        try {
            Dao<Movie, Integer> movieDao = helper.getMovieDao();
            return movieDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void deleteDbMovies() {
        try {
            Dao<Movie, Integer> movieDao = helper.getMovieDao();
            List<Movie> movies = movieDao.queryForAll();
            movieDao.delete(movies);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
