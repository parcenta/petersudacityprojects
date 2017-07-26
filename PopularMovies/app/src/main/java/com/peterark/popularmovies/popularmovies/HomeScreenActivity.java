package com.peterark.popularmovies.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peterark.popularmovies.popularmovies.models.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    // Loading Movies AsyncTask
    LoadMoviesTask loadMoviesTask;

    // Layout Items
    RecyclerView moviesRecyclerView;
    ProgressBar progressBar;
    TextView errorOcurredTextView;
    TextView noMoviesAvailableTextView;

    // Values
    String orderMode;
    MoviesAdapter adapter;
    List<MovieItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Get Layout items
        moviesRecyclerView          = (RecyclerView) findViewById(R.id.movies_recycler_view);
        progressBar                 = (ProgressBar) findViewById(R.id.loading_movies_progress_bar);
        errorOcurredTextView        = (TextView) findViewById(R.id.error_text_view);
        noMoviesAvailableTextView   = (TextView)  findViewById(R.id.no_movies_found_text_view);

        // Setting adapter
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(adapter);

        // Check if there is a savedInstanceState. If there is then we recover the list.
        /*if(savedInstanceState != null) {

        }else{*/
            // Set the Order Mode initially as Most Popular.
            orderMode = Constants.ORDER_BY_MOST_POPULAR;

            // If not, then load the movies from server
            loadMoviesTask = new LoadMoviesTask();
            loadMoviesTask.execute();
        //}
    }

    // --------------------------------------------------------
    //  Loading Movies AsyncTask
    // --------------------------------------------------------
    private class LoadMoviesTask extends AsyncTask<Void,Void,List<MovieItem>> {

        @Override
        protected void onPreExecute() {
            // First Hide the RecyclerView and the Error Message.
            moviesRecyclerView.setVisibility(View.INVISIBLE);
            errorOcurredTextView.setVisibility(View.INVISIBLE);
            noMoviesAvailableTextView.setVisibility(View.INVISIBLE);

            // Show Progress Bar.
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieItem> doInBackground(Void... params) {
            List<MovieItem> itemList = new ArrayList<>();
            itemList.add(new MovieItem.Builder().withMovieId(1).withMovieName("Pelicula 1").withMoviePosterUrl("http://www.theflea.co.nz/wp-content/uploads/2010/02/star-trek-movie-poster.jpg").build());
            itemList.add(new MovieItem.Builder().withMovieId(2).withMovieName("Pelicula 2").withMoviePosterUrl("http://gdj.graphicdesignjunction.com/wp-content/uploads/2011/12/grey-movie-poster.jpg").build());
            itemList.add(new MovieItem.Builder().withMovieId(3).withMovieName("Pelicula 3").withMoviePosterUrl("http://img.moviepostershop.com/the-hangover-movie-poster-2009-1020488737.jpg").build());
            return itemList;
        }

        @Override
        protected void onPostExecute(List<MovieItem> itemList) {
            Log.d(TAG,"ItemList size: " + itemList.size());

            // Hide the Progress Bar
            progressBar.setVisibility(View.INVISIBLE);

            // Set the Adapter List.
            moviesList = itemList;
            adapter.setItemList(moviesList);

            // Depending if the list was loaded correctly, we show it o
            if(moviesList!=null) {
                if (moviesList.size()>0)
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                else
                    noMoviesAvailableTextView.setVisibility(View.VISIBLE);
            }
            else
                errorOcurredTextView.setVisibility(View.VISIBLE);

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(loadMoviesTask!=null)
            loadMoviesTask.cancel(true);
    }

}
