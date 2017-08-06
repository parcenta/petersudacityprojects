package com.peterark.popularmovies.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.peterark.popularmovies.popularmovies.Constants;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    // Base URLs
    private static final String MOVIE_DB_BASE_URL                       = "http://api.themoviedb.org/3/movie/";
    private static final String SEARCH_MOVIES_BY_MOST_POPULAR_BASE_URL  = MOVIE_DB_BASE_URL + "popular";
    private static final String SEARCH_MOVIES_BY_TOP_RATED_BASE_URL     = MOVIE_DB_BASE_URL + "top_rated";

    // Query Parameters
    private static final String API_KEY_QUERY = "api_key";





    public static URL buildUrl(String requestMode, List<String> params) {

        // Init Base Search URL.
        String baseSearchUrl;

        // Depending in the Order Setting, we modify set the base url.
        switch (requestMode){
            case Constants.ORDER_BY_MOST_POPULAR:
                baseSearchUrl = SEARCH_MOVIES_BY_MOST_POPULAR_BASE_URL;
                break;

            case Constants.ORDER_BY_TOP_RATED:
                baseSearchUrl = SEARCH_MOVIES_BY_TOP_RATED_BASE_URL;
                break;

            case Constants.SEARCH_MOVIE_DETAIL_BY_ID:
                if(params == null || params.isEmpty())
                    return null;

                // Add the MovieId at the end of the URL. Example: "http://api.themoviedb.org/3/movie/123456"
                // As specified in the Documentation API (https://developers.themoviedb.org/3/movies?group=movies)
                baseSearchUrl = MOVIE_DB_BASE_URL + params.get(0).trim(); //
                break;
            default:
                return null;
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
    public static String getResponseFromHttpUrl(Context context, URL url) throws Exception {

        if(!isOnline(context))
            return null;

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

    // Taken from StackOverflow post. https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
