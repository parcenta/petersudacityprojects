package com.peterark.popularmovies.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.peterark.popularmovies.popularmovies.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    // Base URLs
    private static final String SEARCH_MOVIES_BY_MOST_POPULAR_BASE_URL  = "http://api.themoviedb.org/3/movie/popular";
    private static final String SEARCH_MOVIES_BY_TOP_RATED_BASE_URL     = "http://api.themoviedb.org/3/movie/top_rated";

    // Query Parameters
    private static final String API_KEY_QUERY = "api_key";





    public static URL buildUrl(String requestMode) {

        // Init Base Search URL.
        String baseSearchUrl;

        // Depending in the Order Setting, we modify set the base url.
        switch (requestMode){
            case Constants.ORDER_BY_MOST_POPULAR:
                baseSearchUrl = SEARCH_MOVIES_BY_MOST_POPULAR_BASE_URL;
                break;
            default:
                baseSearchUrl = SEARCH_MOVIES_BY_TOP_RATED_BASE_URL;
        }

        // Began to build the Uri with base url.
        Uri builtUri = Uri.parse(baseSearchUrl).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, Constants.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     * Source:  Udacity Sunshine Project
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
