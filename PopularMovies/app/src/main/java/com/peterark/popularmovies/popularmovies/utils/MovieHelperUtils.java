package com.peterark.popularmovies.popularmovies.utils;

import com.peterark.popularmovies.popularmovies.models.Movie;
import com.peterark.popularmovies.popularmovies.models.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieHelperUtils {

    private final static String imageBaseUrl = "http://image.tmdb.org/t/p/w185";

    public static List<MovieItem> getMovieListFromJson(String movieJsonString) throws JSONException {

        // Convert the JsonString to JsonObject.
        JSONObject moviesJson = new JSONObject(movieJsonString);

        // Get Results List.
        JSONArray moviesArray = moviesJson.getJSONArray("results");


        // Init List<MovieItem> to be returned.
        List<MovieItem> moviesList = new ArrayList<>();

        // Loop MoviesJsonArray...
        for (int i = 0; i < moviesArray.length(); i++) {

            // Get JsonObject.
            JSONObject oneMovieObject = moviesArray.getJSONObject(i);

            // Extract Values from JsonObject.
            int movieId         = oneMovieObject.getInt("id");
            String movieTitle   = oneMovieObject.getString("original_title");
            String posterUrl    = imageBaseUrl.concat(oneMovieObject.getString("poster_path"));

            moviesList.add(new MovieItem.Builder()
                                        .withMovieId(movieId)
                                        .withMoviePosterUrl(posterUrl)
                                        .build());
        }

        return moviesList;


    }


    public static Movie getMovieDetailFromJson(String movieDetailJsonString) throws JSONException {

        // Convert the JsonString to JsonObject.
        JSONObject movieDetailJson = new JSONObject(movieDetailJsonString);

        // Get Values from JSON.
        String movieTitle           = movieDetailJson.getString("title");
        String movieReleaseDate     = movieDetailJson.getString("release_date");
        int movieRating             = movieDetailJson.getInt("vote_average");
        String movieSynopsis        = movieDetailJson.getString("overview");;
        String moviePosterUrl       = imageBaseUrl.concat(movieDetailJson.getString("poster_path"));

        return new Movie.Builder().withMovieTitle(movieTitle)
                                    .withMovieSynopsis(movieSynopsis)
                                    .withMoviePosterUrl(moviePosterUrl)
                                    .withMovieRating(movieRating)
                                    .withMovieReleaseDate(movieReleaseDate)
                                    .build();


    }
}
