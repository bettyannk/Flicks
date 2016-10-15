package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String releaseDate;
    Double voteAverage;
    int voteCount;
    long id;
    Double popularity;

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w1280/%s", backdropPath);
    }

    public long getId() {
        return id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Boolean isPopularMovie() {
        if(voteAverage < 5.0) {
            return false;
        }
        return true;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.voteCount = jsonObject.getInt("vote_count");
        this.releaseDate = jsonObject.getString("release_date");
        this.id = jsonObject.getLong("id");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> result = new ArrayList<>();
        for(int x = 0; x < array.length(); x++) {
            try {
                result.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
