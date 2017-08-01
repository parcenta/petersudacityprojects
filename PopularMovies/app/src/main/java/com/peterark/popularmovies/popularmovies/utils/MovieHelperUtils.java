package com.peterark.popularmovies.popularmovies.utils;

import com.peterark.popularmovies.popularmovies.models.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PETER on 30/7/2017.
 */

public class MovieHelperUtils {

    public final static String imageBaseUrl = "http://image.tmdb.org/t/p/w185";

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
                                        .withMovieName(movieTitle)
                                        .withMoviePosterUrl(posterUrl)
                                        .build());
        }

        return moviesList;


    }
}
