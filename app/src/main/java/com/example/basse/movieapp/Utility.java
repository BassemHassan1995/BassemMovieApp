package com.example.basse.movieapp;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by basse on 16-Sep-16.
 */
class Utility {

    static String getJsonString(String my_url, String API_KEY, String LANGUAGE, String EXTRAS) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonString = null;
        Uri moviesBuiltUri;
        try {
            if (my_url.contains("search")){
                moviesBuiltUri = Uri.parse(my_url).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("language", LANGUAGE)
                        .appendQueryParameter("query", EXTRAS)
                        .build();

            }
            else if (!EXTRAS.isEmpty()) {
                moviesBuiltUri = Uri.parse(my_url).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("language", LANGUAGE)
                        .appendQueryParameter("page", EXTRAS)
                        .build();
            }
            else{
                moviesBuiltUri = Uri.parse(my_url).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("language", LANGUAGE)
                        .build();
            }

            URL url = null;
            try {
                url = new URL(moviesBuiltUri.toString());
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            Log.v("URI", "Built URI " + moviesBuiltUri.toString());

            assert url != null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(7000);
            Log.v("URL", "URL Connection" + moviesBuiltUri.toString());
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                jsonString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonString = null;
            }
            jsonString = buffer.toString();

        } catch (IOException e) {
            Log.e("GetResultsTask", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            jsonString = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.v("JSON", "Built JSON " + jsonString);
        return jsonString;
    }

    public static ArrayList<Movie> getMoviesFromJson(String moviesJsonString) {
        ArrayList<Movie> movies = new ArrayList<>();
        String RESULTS = "results";
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w500/";
        String IMAGE_SIZE_BIG = "w500/";
        try {
            JSONObject moviesJson = null;
            moviesJson = new JSONObject(moviesJsonString);
            JSONArray results = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject index = results.getJSONObject(i);
                int id = index.getInt("id");
                String title = index.getString("original_title");
                String release_date = index.getString("release_date");
                String vote = index.getString("vote_average");
                String poster_path = index.getString("poster_path");
                String overview = index.getString("overview");
                String backdrop_path = index.getString("backdrop_path");

                JSONArray genres_json = index.getJSONArray("genre_ids");
                int len = genres_json.length();
                int[] genres_ids = new int[len];
                for (int j = 0; j < len; j++) {
                    genres_ids [j] = genres_json.getInt(j);
                }
                String genres = Utility.getMovieGenres(genres_ids);

                String IMAGE_URL = IMAGE_BASE_URL + IMAGE_SIZE + poster_path;
                String BACKDROP_URL = IMAGE_BASE_URL + IMAGE_SIZE_BIG + backdrop_path;

                movies.add(new Movie(id, IMAGE_URL, BACKDROP_URL, overview, release_date, title, vote,genres));
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTrailerFromJson(String trailersJsonString, String YOUTUBE_BASE_URL) {
        String RESULTS = "results";
        String KEY = "";
        String trailer_path = "";

        try {
            JSONObject moviesJson = null;
            moviesJson = new JSONObject(trailersJsonString);
            JSONArray results = moviesJson.getJSONArray(RESULTS);
            JSONObject trailer = results.getJSONObject(0);
            KEY = trailer.getString("key");
            trailer_path = YOUTUBE_BASE_URL + KEY;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailer_path;
    }

    public static HashMap<String, String> getReviewsFromJson(String reviewsJsonString) {
        String RESULTS = "results";
        HashMap<String, String> reviews = new HashMap<>();
        String author;
        String content;

        try {
            JSONObject moviesJson = new JSONObject(reviewsJsonString);
            JSONArray results = moviesJson.getJSONArray(RESULTS);
            for (int index = 0; index < results.length(); index++) {
                JSONObject review = results.getJSONObject(index);
                author = review.getString("author");
                content = review.getString("content");
                reviews.put(author, content);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    //Function to make the expandable list view scrollable inside the scroll view
    public static void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
    public static String getMovieGenres(int[] genreIds) {
        ArrayList<String> genres = new ArrayList<>();
        for (int genreId : genreIds) {
            String genre = "";
            switch (genreId) {
                case 28:
                    genre = "Action";
                    break;
                case 12:
                    genre = "Adventure";
                    break;
                case 16:
                    genre = "Animation";
                    break;
                case 35:
                    genre = "Comedy";
                    break;
                case 80:
                    genre = "Crime";
                    break;
                case 99:
                    genre = "Documentary";
                    break;
                case 18:
                    genre = "Drama";
                    break;
                case 10751:
                    genre = "Family";
                    break;
                case 14:
                    genre = "Fantasy";
                    break;
                case 36:
                    genre = "History";
                    break;
                case 27:
                    genre = "Horror";
                    break;
                case 10402:
                    genre = "Music";
                    break;
                case 9648:
                    genre = "Mystery";
                    break;
                case 10749:
                    genre = "Romance";
                    break;
                case 878:
                    genre = "Science Fiction";
                    break;
                case 10770:
                    genre = "TV Movie";
                    break;
                case 53:
                    genre = "Thriller";
                    break;
                case 10752:
                    genre = "War";
                    break;
                case 37:
                    genre = "Western";
                    break;

                default:
                    break;
            }
            genres.add(genre);
        }
        String genres_string = genres.toString();
        return genres_string.substring(1,genres_string.length()-1);
    }

}
