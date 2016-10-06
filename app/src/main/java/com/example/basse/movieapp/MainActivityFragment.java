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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public String sortType = "popular";
    public ImageAdapter imageAdapter;
    public ArrayList<String> posters_paths = new ArrayList<>();
    public GridView gridView;
    public TextView showMore_textView;
    public ArrayList<Movie> movies = new ArrayList<>();
    public static int page = 1;
    Map<String, Integer> pages_map = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        showMore_textView = (TextView) view.findViewById(R.id.show_more_text_view);
        gridView = (GridView) view.findViewById(R.id.gridview_movies);
        imageAdapter = new ImageAdapter(getContext(), posters_paths, false);
        gridView.setAdapter(imageAdapter);
        if (getArguments() != null && movies.isEmpty()) {
            GetMoviesTask getMoviesTask = new GetMoviesTask();
            sortType = this.getArguments().getString("sort_type", "popular");
            pages_map.put(sortType, page);
            getMoviesTask.execute(sortType, String.valueOf(page));
            pages_map.put(sortType, pages_map.get(sortType) + 1);
        }

        showMore_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMoviesTask getMoviesTask = new GetMoviesTask();
                getMoviesTask.execute(sortType, String.valueOf(pages_map.get(sortType)));
                pages_map.put(sortType, pages_map.get(sortType) + 1);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem != 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // End has been reached
                    showMore_textView.setVisibility(View.VISIBLE);
                } else {
                    showMore_textView.setVisibility(View.GONE);
                }
            }
        });

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
        final String API_KEY = getString(R.string.api_key);
        final String BASE_URL = getString(R.string.baseURL);

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // Will contain the raw JSON response as a string.
            String moviesJsonString;

            String SORT_TYPE = params[0];
            String MOVIES_URL = BASE_URL + SORT_TYPE;
            String LANGUAGE = getString(R.string.english);
            String PAGE = params[1];

            moviesJsonString = Utility.getJsonString(MOVIES_URL, API_KEY, LANGUAGE, PAGE);

            movies.addAll(Utility.getMoviesFromJson(moviesJsonString));

            for (int index = posters_paths.size(); index < movies.size(); index++) {
                Movie movie = movies.get(index);
                Log.v("Poster Paths", "Poster Path " + movie.getPoster_path());
                posters_paths.add(index, movie.getPoster_path());
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            imageAdapter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}