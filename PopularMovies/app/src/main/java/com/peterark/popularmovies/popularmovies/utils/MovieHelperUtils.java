package com.peterark.popularmovies.popularmovies.utils;

import android.database.Cursor;
import android.util.Log;

import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewItem;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideoItem;
import com.peterark.popularmovies.popularmovies.models.MovieDetail;
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


    public static MovieDetail getMovieDetailFromJson(String movieDetailJsonString) throws JSONException {

        // Convert the JsonString to JsonObject.
        JSONObject movieDetailJson = new JSONObject(movieDetailJsonString);

        // Get Values from JSON.
        String movieTitle           = movieDetailJson.getString("title");
        String movieReleaseDate     = movieDetailJson.getString("release_date");
        double movieRating          = movieDetailJson.getDouble("vote_average");
        String movieSynopsis        = movieDetailJson.getString("overview");
        String moviePosterUrl       = imageBigBaseUrl.concat(movieDetailJson.getString("poster_path"));


        return new MovieDetail.Builder().withMovieTitle(movieTitle)
                                    .withMovieSynopsis(movieSynopsis)
                                    .withMoviePosterUrl(moviePosterUrl)
                                    .withMovieRating(String.valueOf(movieRating))
                                    .withMovieReleaseDate(movieReleaseDate)
                                    .build();


    }

    public static MovieDetail getMovieDetailFromCursor(Cursor cursor){

        String movieTitle       = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_NAME));
        String movieReleaseDate = String.valueOf(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_RELEASE_DATE)));
        double movieRating      = cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_USER_RATING));
        String movieSynopsis    = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_SYNOPSIS));
        String moviePosterUrl   = imageBigBaseUrl.concat(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_POSTER_URL)));

        return new MovieDetail.Builder().withMovieTitle(movieTitle)
                .withMovieSynopsis(movieSynopsis)
                .withMoviePosterUrl(moviePosterUrl)
                .withMovieRating(String.valueOf(movieRating))
                .withMovieReleaseDate(movieReleaseDate)
                .withMovieIsFavorite(true) // If it exists in this table, then is a favorite movie.
                .build();

    }

    public static List<VideoItem> getMovieDetailVideoListFromJson(String movieDetailVideoListJson) throws JSONException{

        List<VideoItem> videoList = new ArrayList<>();

        if (movieDetailVideoListJson == null)
            return videoList; // Empty list

        // Convert the JsonString to JsonObject.
        JSONObject videoResponseJsonObject = new JSONObject(movieDetailVideoListJson);

        // Now get the Results.
        String videoResultsString = videoResponseJsonObject.getString("results");
        Log.d("VideoResults",videoResultsString);

        // Source: https://stackoverflow.com/questions/32252606/json-get-list-of-json-objects
        JSONArray videoArray = new JSONArray(videoResultsString);
        for (int i = 0; i < videoArray.length(); i++) { // Walk through the Array.
            JSONObject oneVideoObject = videoArray.getJSONObject(i);
            String videoSourceType = oneVideoObject.getString("site");

            // Just double checking the Site is from Youtube Site.
            if (videoSourceType.equals("YouTube")){
                String videoTitle       = oneVideoObject.getString("type");
                String videoYoutubeId   = oneVideoObject.getString("key").trim();
                String videoUrlString   = videoYoutubeId.length() >0 ? "https://www.youtube.com/watch?v=" + videoYoutubeId : "";

                String videoThumnailUrl = "http://img.youtube.com/vi/"+videoYoutubeId+"/0.jpg";


                videoList.add(new VideoItem(videoTitle,videoUrlString,videoThumnailUrl));
            }

        }

        return videoList;
    }

    public static List<ReviewItem> getMovieDetailReviewsListFromJson(String movieDetailReviewsListJson) throws JSONException{

        List<ReviewItem> reviewList = new ArrayList<>();

        if (movieDetailReviewsListJson == null)
            return reviewList; // Returning empty list.

        // Convert the JsonString to JsonObject.
        JSONObject reviewsResponseJsonObject = new JSONObject(movieDetailReviewsListJson);

        // Now get the Results.
        String reviewsResultsString = reviewsResponseJsonObject.getString("results");

        // Now from the Results we parse it as JsonArray.
        // Source: https://stackoverflow.com/questions/32252606/json-get-list-of-json-objects
        JSONArray reviewArray = new JSONArray(reviewsResultsString);
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject oneVideoObject = reviewArray.getJSONObject(i);
            String reviewUserName       = oneVideoObject.getString("author");
            String reviewUserCommentary = oneVideoObject.getString("content");
            reviewList.add(new ReviewItem(reviewUserName,reviewUserCommentary));
        }

        return reviewList;
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
