package com.example.basse.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class TabletPostersFragment extends Fragment {

    private GridView gridView;
    public ArrayList<Movie> movies = new ArrayList<>();
    public ArrayList<ImageView> postersList = new ArrayList<ImageView>();
    public ImageAdapter imageAdapter;
    public Communicator communicator;
    public static int currentPosition = 0;
    public static boolean favoriteFragment = false;

    public TabletPostersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (Communicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tablet_posters, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview_movies);
        postersList.add(new ImageView(getContext()));
        imageAdapter = new ImageAdapter(getContext(), postersList);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                if (!isTablet(getActivity())) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("Position", position);
                    intent.putExtra("Movie", movies.get(position));
                    startActivity(intent);
                } else {
                    communicator.sendData(movies.get(position));
                }

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_types, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated:
                update("top_rated");
                break;
            case R.id.most_popular:
                update("popular");
                break;
            case R.id.favourite:
                favoriteFragment = true;
                update("favorites");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void update(String sortType) {
        if (sortType.equals("favorites")) {
            movies = (ArrayList<Movie>) Utility.getDbMovies();
            if (movies != null) {
                updatePosters(movies, postersList);
            }
        } else {
            GetMoviesTask getMoviesTask = new GetMoviesTask();
            getMoviesTask.execute(sortType);
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        public final String API_KEY = getString(R.string.api_key);
        public final String BASE_URL = getString(R.string.baseURL);

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            String SORT_TYPE = params[0];
            String MOVIES_URL = BASE_URL + SORT_TYPE;

            moviesJsonString = Utility.getJsonString(MOVIES_URL, API_KEY);
            Log.v("Movie", "Movies JSON" + moviesJsonString);

            movies = Utility.getMoviesFromJson(moviesJsonString);

            return movies;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (isAdded()) {
                updatePosters(movies, postersList);
                communicator.sendData(movies.get(currentPosition));
                //addMoviesToDb(movies);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (movies.size() != 0) {
            communicator.sendData(movies.get(currentPosition));
        }
    }

    private void updatePosters(ArrayList<Movie> movies, ArrayList<ImageView> posters) {
        posters.clear();
        for (int index = 0; index < movies.size(); index++) {
            Movie movie = movies.get(index);
            Log.v("Poster Paths", "Poster Path " + movie.getPoster_path());
            ImageView poster = Utility.getTabletPoster(getContext(), movie);
            postersList.add(index, poster);
        }
        imageAdapter.notifyDataSetChanged();
    }
}
