package com.peterark.popularmovies.popularmovies.database.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.peterark.popularmovies.popularmovies.R;

/**
 * Created by PETER on 25/9/2017.
 */

public class FavoriteMoviesContract {


    public static final String CONTENT_AUTHORITY  = "com.peterark.popularmovies";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoritemovies";

    public static final class FavoritesMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();


        // TABLE NAME
        public static final String TABLE_NAME = "favoritemovies";

        // COLUMNS
        public static final String COLUMN_MOVIE_ID              = "movieid";
        public static final String COLUMN_MOVIE_NAME            = "moviename";
        public static final String COLUMN_MOVIE_SYNOPSIS        = "moviesynopsis";
        public static final String COLUMN_MOVIE_USER_RATING     = "movieuserating";
        public static final String COLUMN_MOVIE_RELEASE_DATE    = "moviereleasedate";
        public static final String COLUMN_MOVIE_POSTER_URL      = "movieposterurl";

        // FOR CONTENT PROVIDER: URI WITH FAVORITE MOVIE ID
        public static Uri buildUriForFavoriteMovieWithId(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .build();
        }

    }

}
