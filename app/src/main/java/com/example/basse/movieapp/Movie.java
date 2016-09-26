package com.example.basse.movieapp;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by basse on 31-Jul-16.
 */
@DatabaseTable(tableName = Movie.TABLE_NAME_MOVIES)
public class Movie implements Serializable {

    public static final String TABLE_NAME_MOVIES = "users";

    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_OVERVIEW = "overview";
    public static final String FIELD_RELEASE_DATE = "release_date";
    public static final String FIELD_VOTE_AVERAGE = "vote_average";
    public static final String FIELD_POSTER_PATH = "poster_path";
    public static final String FIELD_BACKDROP_PATH = "backdrop_path";
    public static final String FIELD_POSTER = "poster";
    public static final String FIELD_BACKDROP = "backdrop";
    public static final String FIELD_TRAILER_PATH = "trailer";
    public static final String FIELD_REVIEWS = "reviews";
    public static final String FIELD_FAVOURITE = "favourite";

    @DatabaseField(columnName = FIELD_ID, id = true)
    private int id;

    @DatabaseField(columnName = FIELD_TITLE)
    private String title;

    @DatabaseField(columnName = FIELD_OVERVIEW)
    private String overview;

    @DatabaseField(columnName = FIELD_RELEASE_DATE)
    private String release_date;

    @DatabaseField(columnName = FIELD_VOTE_AVERAGE)
    private String vote_average;

    @DatabaseField(columnName = FIELD_POSTER_PATH)
    private String poster_path;

    @DatabaseField(columnName = FIELD_BACKDROP_PATH)
    private String backdrop_path;

    @DatabaseField(columnName = FIELD_TRAILER_PATH)
    private String trailerPath;

    @DatabaseField(columnName = FIELD_REVIEWS, dataType = DataType.SERIALIZABLE)
    private HashMap<String, String> reviews;

    @DatabaseField(columnName = FIELD_FAVOURITE)
    private boolean favourite ;

    public Movie() {
    }

    public Movie(int id, String poster_path, String backdrop_path, String overview, String release_date, String title, String vote_average) {
        this.id = id;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
        this.trailerPath = "";
        this.reviews = new HashMap<>();
        favourite = false;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, String> reviews) {
        this.reviews = reviews;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
