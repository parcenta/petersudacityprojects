package com.peterark.popularmovies.popularmovies.database;

import android.database.sqlite.SQLiteDatabase;

import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;

/**
 * Created by PETER on 26/9/2017.
 */

public class PopularMoviesDbUtils {

    public static void createTable(SQLiteDatabase db, String tableName){

        String CREATE_TABLE_SQL = null;

        switch (tableName){
            case FavoriteMoviesContract.FavoritesMoviesEntry.TABLE_NAME:
                CREATE_TABLE_SQL = "CREATE TABLE "  + FavoriteMoviesContract.FavoritesMoviesEntry.TABLE_NAME + " (" +
                                FavoriteMoviesContract.FavoritesMoviesEntry._ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_ID             + " INTEGER NOT NULL, " +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_NAME           + " TEXT NOT NULL," +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_SYNOPSIS       + " INTEGER NOT NULL," +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_USER_RATING    + " REAL NOT NULL," +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_POSTER_URL     + " TEXT NOT NULL," +
                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_RELEASE_DATE   + " INTEGER NOT NULL);";
                break;
        }

        if (CREATE_TABLE_SQL != null)
            db.execSQL(CREATE_TABLE_SQL);
    }

}
