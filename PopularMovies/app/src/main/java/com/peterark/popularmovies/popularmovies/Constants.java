package com.peterark.popularmovies.popularmovies;

import android.content.Context;

public class Constants {

    // TODO: Take it out when uploading to repository
    public static final String MOVIE_DB_API_KEY = "API_KEY"; // YOUR_API_KEY_HERE

    public static final String ORDER_BY_MOST_POPULAR        = "ORDER_BY_MOST_POPULAR";
    public static final String ORDER_BY_TOP_RATED           = "ORDER_BY_TOP_RATED";
    public static final String SEARCH_MOVIE_DETAIL_BY_ID    = "SEARCH_MOVIE_DETAIL_BY_ID";

    //
    public static final int MAX_MOVIE_RATING    = 10;


    public static String orderModeDescription(Context context, String orderMode){

        if(context == null)
            return "";

        switch (orderMode){
            case ORDER_BY_TOP_RATED:
                return context.getString(R.string.top_rated_description);
            default:
                return context.getString(R.string.most_popular_description);
        }
    }

}
