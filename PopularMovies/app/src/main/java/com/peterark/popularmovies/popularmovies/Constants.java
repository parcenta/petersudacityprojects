package com.peterark.popularmovies.popularmovies;

import android.content.Context;

/**
 * Created by PETER on 26/7/2017.
 */

public class Constants {

    // TODO: Take it out when uploading to Github
    public static String MOVIE_DB_API_KEY = "YOUR_API_KEY_HERE"; // YOUR_API_KEY_HERE

    public static final String ORDER_BY_MOST_POPULAR  = "ORDER_BY_MOST_POPULAR";
    public static final String ORDER_BY_TOP_RATED     = "ORDER_BY_TOP_RATED";


    public String orderModeDescription(Context context, String orderMode){
        switch (orderMode){
            case ORDER_BY_TOP_RATED:
                return context.getString(R.string.top_rated_description);
            default:
                return context.getString(R.string.most_popular_description);
        }
    }

}
