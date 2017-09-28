package com.peterark.popularmovies.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;

/**
 * Created by PETER on 26/9/2017.
 */

public class PopularMoviesDbHelper  extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "popularmoviesdb.db";
    private static final int VERSION = 1;

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        PopularMoviesDbUtils.createTable(db, FavoriteMoviesContract.FavoritesMoviesEntry.TABLE_NAME);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
