package com.peterark.popularmovies.popularmovies;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;
import com.peterark.popularmovies.popularmovies.databinding.ActivityHomeScreenBinding;
import com.peterark.popularmovies.popularmovies.detailPanel.MovieDetailActivity;
import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity
                        implements MoviesAdapter.OnMovieClickHandler,
                                    LoaderManager.LoaderCallbacks<List<MovieItem>>{

    //
    private final String TAG = this.getClass().getSimpleName();
    private final String MOVIES_LIST  = "ITEM_LIST";
    private final String ORDER_BY   = "ORDER_BY";

    private ActivityHomeScreenBinding mBinding;

    // Values
    private String orderMode;
    private MoviesAdapter adapter;
    private List<MovieItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Binding
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen);

        // Setting adapter
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mBinding.moviesRecyclerView.setLayoutManager(layoutManager);
        mBinding.moviesRecyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter(this,this);
        mBinding.moviesRecyclerView.setAdapter(adapter);

        // Set Retry Action on Error Message Textview.
        mBinding.errorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovies();
            }
        });

        // Check if there is a savedInstanceState. If there is then we recover the list.
        if(savedInstanceState != null && savedInstanceState.containsKey(ORDER_BY))
            orderMode   = savedInstanceState.getString(ORDER_BY);
        else
            orderMode = Constants.ORDER_BY_MOST_POPULAR; // Default

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(0,null,this);
    }

    private void loadMovies(){
        // Set in the ActionBar title, by what order are the movies.
        refreshActionBarTitle();

        // Restart the loader to load the movies.
        getSupportLoaderManager().restartLoader(0,null,this);
    }

    private void refreshActionBarTitle(){
        String orderModeDescription = Constants.orderModeDescription(this,orderMode);
        getSupportActionBar().setTitle(orderModeDescription); // I manage the possible NullPointer inside the above method.
    }

    // --------------------------------------------------------
    //  Menu Stuff
    // --------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.order_by_most_popular:
                orderMode = Constants.ORDER_BY_MOST_POPULAR;
                loadMovies();
                break;
            case R.id.order_by_top_rated:
                orderMode = Constants.ORDER_BY_TOP_RATED;
                loadMovies();
                break;
            case R.id.order_by_favorite:
                orderMode = Constants.ORDER_BY_FAVORITE;
                loadMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieItem item) {

        if (item == null) {
            Toast.makeText(this, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
            return;
        }

        // Launch the MovieDetailActivity with the selected movieId.
        MovieDetailActivity.launch(this,item.movieId());
    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<MovieItem>>(this){

            List<MovieItem> cachedMovieItemList;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                // Set the MoviesList to null.
                moviesList = null;

                // Empty (null) the Item List inside the adapter.
                adapter.setItemList(null);

                // First Hide the RecyclerView and the Error Message.
                mBinding.moviesRecyclerView.setVisibility(View.INVISIBLE);
                mBinding.errorTextView.setVisibility(View.INVISIBLE);
                mBinding.noMoviesFoundTextView.setVisibility(View.INVISIBLE);

                // Show Progress Bar.
                mBinding.loadingMoviesProgressBar.setVisibility(View.VISIBLE);


                if(cachedMovieItemList!=null)
                    deliverResult(cachedMovieItemList);
                else
                    forceLoad();
            }



            @Override
            public List<MovieItem> loadInBackground() {

                List<MovieItem> itemList = new ArrayList<>();

                switch (orderMode){

                    // If OrderMode = "Favorites" then we check for the Movies list sved in the DB.
                    case Constants.ORDER_BY_FAVORITE:

                        Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoritesMoviesEntry.CONTENT_URI,null,null,null,null);

                        if(cursor!=null) {
                            while (cursor.moveToNext()) {
                                int movieId             = cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_ID));
                                String moviePosterUrl   = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_POSTER_URL));
                                itemList.add(new MovieItem.Builder().withMovieId(movieId)
                                        .withMoviePosterUrl(MovieHelperUtils.imageBaseUrl.concat(moviePosterUrl))
                                        .build());
                            }
                            cursor.close();
                        }

                        return new ArrayList<>(itemList);

                    // If we are not searching in "Favorite" Mode, then we call the WS.
                    default:
                        // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
                        URL weatherRequestUrl = NetworkUtils.buildUrl(orderMode,null);

                        try {
                            String response = NetworkUtils
                                    .getResponseFromHttpUrl(HomeScreenActivity.this,weatherRequestUrl);

                            if(response==null)
                                return null;

                            Log.d(TAG,"JsonString Response: " + response);

                            itemList = MovieHelperUtils.getMovieListFromJson(response);

                            return new ArrayList<>(itemList);

                        } catch (Exception e) {
                            Log.d(TAG,"Exception was thrown when loading movies...");
                            e.printStackTrace();
                            return null;
                        }
                }
            }

            @Override
            public void deliverResult(List<MovieItem> itemList) {
                cachedMovieItemList = itemList;
                super.deliverResult(itemList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> itemList) {
        // Hide the Progress Bar
        mBinding.loadingMoviesProgressBar.setVisibility(View.INVISIBLE);

        // Set the Adapter List.
        moviesList = itemList;
        adapter.setItemList(moviesList);

        // Depending if the list was loaded correctly, we show it o
        if(moviesList!=null) {
            Log.d(TAG,"ItemList size: " + itemList.size());
            if (moviesList.size()>0)
                mBinding.moviesRecyclerView.setVisibility(View.VISIBLE);
            else
                mBinding.noMoviesFoundTextView.setVisibility(View.VISIBLE);
        }
        else
            mBinding.errorTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ORDER_BY,orderMode);
        super.onSaveInstanceState(outState);
    }
}
