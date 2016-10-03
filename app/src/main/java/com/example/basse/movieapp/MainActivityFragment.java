package com.example.basse.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public String sortType = "popular";
    public ImageAdapter imageAdapter;
    public ArrayList<ImageView> posters = new ArrayList<ImageView>();
    public GridView gridView;
    public ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview_movies);
        posters.add(new ImageView(getContext()));
        imageAdapter = new ImageAdapter(getContext(), posters,false);
        gridView.setAdapter(imageAdapter);

        if (getArguments() != null && movies.isEmpty()) {
            GetMoviesTask getMoviesTask = new GetMoviesTask();
            sortType = this.getArguments().getString("sort_type", "popular");
            getMoviesTask.execute(sortType);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("Position", position);
                intent.putExtra("Movie", movies.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        public final String API_KEY = getString(R.string.api_key);
        public final String BASE_URL = getString(R.string.baseURL);

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            String SORT_TYPE = params[0];
            String MOVIES_URL = BASE_URL + SORT_TYPE;

            moviesJsonString = Utility.getJsonString(MOVIES_URL, API_KEY);

            movies = Utility.getMoviesFromJson(moviesJsonString);

            return movies;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            updatePosters(movies, posters);
            //addMoviesToDb(movies);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

        private void updatePosters(ArrayList<Movie> movies, ArrayList<ImageView> posters) {
            posters.clear();
            for (int index = 0; index < movies.size(); index++) {
                Movie movie = movies.get(index);
                Log.v("Poster Paths", "Poster Path " + movie.getPoster_path());
                ImageView poster = Utility.getPoster(getContext(), movie);
                MainActivityFragment.this.posters.add(index, poster);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }
}