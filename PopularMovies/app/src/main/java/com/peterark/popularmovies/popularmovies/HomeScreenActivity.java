package com.peterark.popularmovies.popularmovies;

import android.os.AsyncTask;
import android.os.Parcelable;
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

import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    //
    private final String TAG = this.getClass().getSimpleName();
    private final String MOVIES_LIST  = "ITEM_LIST";
    private final String ORDER_BY   = "ORDER_BY";


    // Loading Movies AsyncTask
    private LoadMoviesTask loadMoviesTask;

    // Layout Items
    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;
    private TextView errorOccurredTextView;
    private TextView noMoviesAvailableTextView;

    // Values
    private String orderMode;
    private MoviesAdapter adapter;
    private ArrayList<MovieItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Get Layout items
        moviesRecyclerView          = (RecyclerView) findViewById(R.id.movies_recycler_view);
        progressBar                 = (ProgressBar) findViewById(R.id.loading_movies_progress_bar);
        errorOccurredTextView       = (TextView) findViewById(R.id.error_text_view);
        noMoviesAvailableTextView   = (TextView)  findViewById(R.id.no_movies_found_text_view);

        // Setting adapter
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(adapter);

        // Check if there is a savedInstanceState. If there is then we recover the list.
        if(savedInstanceState != null
                && savedInstanceState.containsKey(ORDER_BY)
                && savedInstanceState.containsKey(MOVIES_LIST)) {

            // Get SavedInstance variables
            orderMode   = savedInstanceState.getString(ORDER_BY);
            moviesList  = savedInstanceState.getParcelableArrayList(MOVIES_LIST);

            // Now with the restored variables an update the UI (Action Bar and Recycler View)
            refreshActionBarTitle();
            adapter.setItemList(moviesList);
        }else{
            // Set the Order Mode initially as Most Popular.
            orderMode = Constants.ORDER_BY_MOST_POPULAR;

            // If not, then load the movies from server
            loadMovies();
        }

    }


    private void loadMovies(){
        // Set in the ActionBar title, by what order are the movies.
        refreshActionBarTitle();

        // Cancel previous request
        cancelLoadingMovies();

        // Load Movies.
        loadMoviesTask = new LoadMoviesTask();
        loadMoviesTask.execute();
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
        }

        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------------------------
    //  Loading Movies AsyncTask
    // --------------------------------------------------------
    private class LoadMoviesTask extends AsyncTask<Void,Void,ArrayList<MovieItem>> {

        @Override
        protected void onPreExecute() {
            // Empty (null) the Item List inside the adapter.
            adapter.setItemList(null);

            // First Hide the RecyclerView and the Error Message.
            moviesRecyclerView.setVisibility(View.INVISIBLE);
            errorOccurredTextView.setVisibility(View.INVISIBLE);
            noMoviesAvailableTextView.setVisibility(View.INVISIBLE);

            // Show Progress Bar.
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieItem> doInBackground(Void... params) {

            // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
            URL weatherRequestUrl = NetworkUtils.buildUrl(orderMode);

            try {
                String response = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                Log.d(TAG,"JsonString Response: " + response);

                return new ArrayList<>(MovieHelperUtils.getMovieListFromJson(response));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> itemList) {

            // Hide the Progress Bar
            progressBar.setVisibility(View.INVISIBLE);

            // Set the Adapter List.
            moviesList = itemList;
            adapter.setItemList(moviesList);

            // Depending if the list was loaded correctly, we show it o
            if(moviesList!=null) {
                Log.d(TAG,"ItemList size: " + itemList.size());
                if (moviesList.size()>0)
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                else
                    noMoviesAvailableTextView.setVisibility(View.VISIBLE);
            }
            else
                errorOccurredTextView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelLoadingMovies();
    }

    private void cancelLoadingMovies(){
        if(loadMoviesTask!=null)
            loadMoviesTask.cancel(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ORDER_BY,orderMode);
        outState.putParcelableArrayList(MOVIES_LIST,moviesList);
        super.onSaveInstanceState(outState);
    }
}
