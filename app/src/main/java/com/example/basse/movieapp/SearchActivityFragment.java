package com.example.basse.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    public ImageAdapter imageAdapter;
    public ArrayList<String> posters_paths = new ArrayList<>();
    public GridView gridView;
    public ArrayList<Movie> movies = new ArrayList<>();
    public TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview_movies);
        gridView.setEmptyView(view.findViewById(R.id.empty_view));
        imageAdapter = new ImageAdapter(getContext(), posters_paths, false);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("Position", position);
                intent.putExtra("Movie", movies.get(position));
                startActivity(intent);
            }
        });
        textView = (TextView) view.findViewById(R.id.empty_text);
        textView.setText("Search for a movie");
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                textView.setText("No results found");
                GetResultsTask getResultsTask = new GetResultsTask();
                getResultsTask.execute(search);
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_search || super.onOptionsItemSelected(item);

    }


    public class GetResultsTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        final String API_KEY = getString(R.string.api_key);
        final String BASE_URL = getString(R.string.search_base_URL);

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Finding Results...");
            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // Will contain the raw JSON response as a string.
            String moviesJsonString;

            String SEARCH = params[0];
            String LANGUAGE = getString(R.string.english);

            moviesJsonString = Utility.getJsonString(BASE_URL, API_KEY, LANGUAGE, SEARCH);

            posters_paths.clear();
            movies = Utility.getMoviesFromJson(moviesJsonString);
            for (int index = 0; index < movies.size(); index++) {
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
