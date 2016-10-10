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

    static final String TABLE_NAME_MOVIES = "users";

    private static final String FIELD_ID = "id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_OVERVIEW = "overview";
    private static final String FIELD_RELEASE_DATE = "release_date";
    private static final String FIELD_VOTE_AVERAGE = "vote_average";
    private static final String FIELD_POSTER_PATH = "poster_path";
    private static final String FIELD_BACKDROP_PATH = "backdrop_path";
    private static final String FIELD_TRAILER_PATH = "trailer";
    private static final String FIELD_REVIEWS = "reviews";
    private static final String FIELD_FAVOURITE = "favourite";
    private static final String FIELD_GENRES = "genres";

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

//    @DatabaseField(columnName = FIELD_GENRES)
    private String genres;

    @DatabaseField(columnName = FIELD_REVIEWS, dataType = DataType.SERIALIZABLE)
    private HashMap<String, String> reviews;

    @DatabaseField(columnName = FIELD_FAVOURITE)
    private boolean favourite;

    public Movie() {
    }

    public Movie(int id, String poster_path, String backdrop_path, String overview, String release_date, String title, String vote_average,String genres) {
        this.id = id;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
        this.trailerPath = "";
        this.reviews = new HashMap<>();
        this.genres = genres;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
