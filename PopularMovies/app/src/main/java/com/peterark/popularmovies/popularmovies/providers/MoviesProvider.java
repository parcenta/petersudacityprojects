package com.peterark.popularmovies.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.peterark.popularmovies.popularmovies.database.PopularMoviesDbHelper;
import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;

/**
 * Created by PETER on 26/9/2017.
 */

public class MoviesProvider extends ContentProvider {

    private static final String MOVIES_TABLE_NAME = FavoriteMoviesContract.FavoritesMoviesEntry.TABLE_NAME;

    public static final int CODE_FAVORITEMOVIES         = 1000;
    public static final int CODE_FAVORITEMOVIES_WITH_ID = 1001;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private PopularMoviesDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, CODE_FAVORITEMOVIES);
        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", CODE_FAVORITEMOVIES_WITH_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }


    // --------------------------------------------------------------------------------
    //  QUERY
    // --------------------------------------------------------------------------------
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITEMOVIES: {

                cursor = mDbHelper.getReadableDatabase().query(
                        MOVIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAVORITEMOVIES_WITH_ID: {

                String favoriteMovieId = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{favoriteMovieId};

                cursor = mDbHelper.getReadableDatabase().query(
                        MOVIES_TABLE_NAME,
                        projection,
                        FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    // --------------------------------------------------------------------------------
    //  INSERT
    // --------------------------------------------------------------------------------
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case CODE_FAVORITEMOVIES:
                long id = mDb.insert(MOVIES_TABLE_NAME,null,values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoritesMoviesEntry.CONTENT_URI,id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unkown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }


    // --------------------------------------------------------------------------------
    //  DELETE
    // --------------------------------------------------------------------------------
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITEMOVIES_WITH_ID:

                String favoriteMovieId = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{favoriteMovieId};

                rowsDeleted = mDbHelper.getWritableDatabase().delete(
                                                                FavoriteMoviesContract.FavoritesMoviesEntry.TABLE_NAME,
                                                                FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                                                                selectionArguments);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}
