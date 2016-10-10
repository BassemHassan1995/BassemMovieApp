package com.example.basse.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    HashMap<String, String> mapDataChild = new HashMap<>();
    Movie movie = new Movie();
    boolean isFavourite = false;
    private TextView title_textView;
    private TextView releaseDate_textView;
    private TextView overview_textView;
    private ImageView backdropPoster;
    private ImageView playTrailer;
    private RatingBar ratingBar;
    private TextView genre_TextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        final Intent intent = getActivity().getIntent();
        title_textView = (TextView) view.findViewById(R.id.title_text_view);
        releaseDate_textView = (TextView) view.findViewById(R.id.release_date_text_view);
        overview_textView = (TextView) view.findViewById(R.id.overview_text_view);
        backdropPoster = (ImageView) view.findViewById(R.id.poster_image_view);
        playTrailer = (ImageView) view.findViewById(R.id.play_button);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        genre_TextView = (TextView) view.findViewById(R.id.genre_text_view);

        if (intent != null && intent.hasExtra("Position")) {
            movie = (Movie) intent.getSerializableExtra("Movie");

            TrailerAndReviewsTask trailerAndReviewsTask = new TrailerAndReviewsTask();
            trailerAndReviewsTask.execute(movie);

            String title = movie.getTitle();
            String releaseDate = movie.getRelease_date();
            String genre = movie.getGenres();
            String overview = movie.getOverview();
            String backdrop = movie.getBackdrop_path();
            isFavourite = movie.isFavourite();
            final float rating = Float.parseFloat(movie.getVote_average()) ;

            title_textView.setText(title);
            releaseDate_textView.setText(releaseDate);
            genre_TextView.setText(genre);
            overview_textView.setText(overview);
            if (!backdrop.contains("null")) {
                Picasso.with(getContext()).load(backdrop).into(backdropPoster);
            }

            playTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String trailerPath = movie.getTrailerPath();
                    if (trailerPath.isEmpty())
                    {
                        Toast.makeText(getContext(), R.string.no_trailers, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerPath)));
                    }

                }
            });

            ratingBar.setRating(rating);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getContext(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            expListView.setEmptyView(view.findViewById(R.id.reviews_text_view));
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                int previousItem = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousItem)
                        expListView.collapseGroup(previousItem);
                    previousItem = groupPosition;
                }
            });

        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_favourite);
        Dao<Movie, Integer> movieDao;
        try {
            movieDao = MainActivity.helper.getMovieDao();
            Movie myMovie = movieDao.queryForSameId(movie);
            if (myMovie != null && myMovie.isFavourite()) {
                menuItem.setIcon(R.drawable.ic_star_white_48dp);
            } else
                menuItem.setIcon(R.drawable.ic_star_border_white_48dp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            item.setTitle("Share Trailer");
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, movie.getTrailerPath());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        if (id == R.id.action_favourite) {
            if (!isFavourite) {
                isFavourite = true;
                movie.setFavourite(true);
                addMovieToDb();
                item.setIcon(R.drawable.ic_star_white_48dp);
                Toast.makeText(getContext(), "Added To Favourites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Movie is already added To Favourites", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TrailerAndReviewsTask extends AsyncTask<Movie, Void, Movie> {
        final String API_KEY = getString(R.string.api_key);
        final String BASE_URL = getString(R.string.baseURL);
        final String VIDEOS = getString(R.string.videos);
        final String REVIEWS = getString(R.string.reviews);
        final String YOUTUBE_BASE_URL = getString(R.string.youtube_base_url);
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting Details");
            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Movie doInBackground(Movie... params) {
            Movie myMovie = params[0];
            String movieId = String.valueOf(myMovie.getId());

            String REVIEWS_URL = BASE_URL + movieId + '/' + REVIEWS;
            String TRAILERS_URL = BASE_URL + movieId + '/' + VIDEOS;
            String LANGUAGE = getString(R.string.english);
            String PAGE = "";

            String reviewsJsonString = Utility.getJsonString(REVIEWS_URL, API_KEY, LANGUAGE ,PAGE);
            String trailersJsonString = Utility.getJsonString(TRAILERS_URL, API_KEY, LANGUAGE, PAGE);

            HashMap<String, String> reviews = Utility.getReviewsFromJson(reviewsJsonString);
            myMovie.setReviews(reviews);
            String trailer = Utility.getTrailerFromJson(trailersJsonString, YOUTUBE_BASE_URL);

            myMovie.setTrailerPath(trailer);

            return myMovie;
        }


        @Override
        protected void onPostExecute(Movie myMovie) {

            mapDataChild.clear();
            movie = myMovie;
            mapDataChild = movie.getReviews();
            listAdapter = new ExpandableListAdapter(getActivity(), mapDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Utility.setListViewHeight(parent, groupPosition);
                    return false;
                }
            });

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public boolean addMovieToDb() {
        MovieDbHelper helper = MainActivity.helper;
        try {
            Dao<Movie, Integer> movieDao = helper.getMovieDao();
            movieDao.createIfNotExists(movie);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
