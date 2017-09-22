package com.peterark.popularmovies.popularmovies.utils;

import android.util.Log;

import com.peterark.popularmovies.popularmovies.models.Movie;
import com.peterark.popularmovies.popularmovies.models.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MovieHelperUtils {

    private static final String TAG = MovieHelperUtils.class.getSimpleName();

    private final static String imageBaseUrl = "http://image.tmdb.org/t/p/w185";
    private final static String imageBigBaseUrl = "http://image.tmdb.org/t/p/w342";

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
        double movieRating          = movieDetailJson.getDouble("vote_average");
        String movieSynopsis        = movieDetailJson.getString("overview");
        String moviePosterUrl       = imageBigBaseUrl.concat(movieDetailJson.getString("poster_path"));

        return new Movie.Builder().withMovieTitle(movieTitle)
                                    .withMovieSynopsis(movieSynopsis)
                                    .withMoviePosterUrl(moviePosterUrl)
                                    .withMovieRating(movieRating)
                                    .withMovieReleaseDate(movieReleaseDate)
                                    .build();


    }

    // Converting one Date (as string) to another format.
    // As specified in https://stackoverflow.com/questions/12503527/how-do-i-convert-the-date-from-one-format-to-another-date-object-in-another-form
    public static String getDateAsMMMDDYYYYWithMonthName(String dateStringAsYYYYMMDD){
        try {
            Log.d(TAG,"Converting date from: " + dateStringAsYYYYMMDD);
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy",Locale.getDefault());
            Date date = originalFormat.parse(dateStringAsYYYYMMDD);
            Log.d(TAG,"Original date: " + date);
            return targetFormat.format(date);
        }catch (Exception e){
            return dateStringAsYYYYMMDD;
        }
    }
}
