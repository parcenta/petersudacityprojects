package com.peterark.popularmovies.popularmovies.detailPanel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.models.Movie;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {


    // Intent Extras names
    public final static String MOVIE_ID = "MOVIE_ID";

    // Intent variables
    public static int mMovieId;

    // Movie
    private Movie movieDetail;

    // For Saved Instance
    private static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    // Asynctask
    LoadMovieDetailTask loadMovieDetailTask;

    // Layout items
    ProgressBar progressBar;
    TextView errorOcurredTextView;
    ScrollView movieDetailContainer;
    TextView movieTitleTextView;
    ImageView posterImageView;
    TextView releaseDateTextView;
    TextView ratingTextView;
    TextView synopsisTextView;

    /* -----------------------------------------------------------------
     * Launch Helper
     * -----------------------------------------------------------------*/
    public static void launch(Context context, int movieId) {
        context.startActivity(launchIntent(context, movieId));
    }

    public static Intent launchIntent(Context context, int movieId) {

        Class destinationActivity = MovieDetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra(MOVIE_ID, movieId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get Layout Items
        progressBar             = (ProgressBar) findViewById(R.id.loading_movie_detail_progress_bar);
        errorOcurredTextView    = (TextView) findViewById(R.id.error_text_view);;
        movieDetailContainer    = (ScrollView) findViewById(R.id.movie_detail_container);
        movieTitleTextView      = (TextView) findViewById(R.id.movie_title_text_view);
        posterImageView         = (ImageView) findViewById(R.id.poster_holder);
        releaseDateTextView     = (TextView) findViewById(R.id.movie_release_date_text_view);
        ratingTextView          = (TextView) findViewById(R.id.movie_rating_text_view);
        synopsisTextView        = (TextView) findViewById(R.id.movie_synopsis_text_view);

        // Get Intent parameters variables
        getParameters();

        // Load Data in Activity
        if(savedInstanceState!=null && savedInstanceState.containsKey(MOVIE_DETAIL)){
            movieDetail = savedInstanceState.getParcelable(MOVIE_DETAIL);
            refreshMovieUI();
        }else
            loadMovieDetail();

    }

    private void loadMovieDetail(){
        if(loadMovieDetailTask!=null)
            loadMovieDetailTask.cancel(true);
        loadMovieDetailTask = new LoadMovieDetailTask();
        loadMovieDetailTask.execute();
    }

    private void getParameters(){
        Intent sourceIntent = getIntent();
        if (sourceIntent.hasExtra(MOVIE_ID))
            mMovieId = sourceIntent.getIntExtra(MOVIE_ID,0);
    }


    // --------------------------------------------------------
    //  Loading Movies AsyncTask
    // --------------------------------------------------------
    private class LoadMovieDetailTask extends AsyncTask<Void,Void,Movie> {

        @Override
        protected void onPreExecute() {
            // First Hide the MovieDetailContainer and the Error Message.
            movieDetailContainer.setVisibility(View.INVISIBLE);
            errorOcurredTextView.setVisibility(View.INVISIBLE);

            // Show Progress Bar.
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(Void... params) {

            // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
            List<String> parametersList = new ArrayList<>();
            parametersList.add(mMovieId + "");
            URL weatherRequestUrl = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_BY_ID,parametersList);

            try {
                String response = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);


                return MovieHelperUtils.getMovieDetailFromJson(response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movieResponse){

            // Set the Adapter List.
            movieDetail = movieResponse;

            if(movieDetail!=null){
                showMovieDetailContainerInUI();
                refreshMovieUI();
            }else
                showErrorMessageInUI();

        }
    }


    private void refreshMovieUI(){

        if(movieDetail==null) {
            showErrorMessageInUI();
            return;
        }

        movieTitleTextView.setText(movieDetail.movieTitle());
        releaseDateTextView.setText(movieDetail.movieReleaseDate());
        ratingTextView.setText(movieDetail.movieRating());
        synopsisTextView.setText(movieDetail.movieSynopsis());
        Picasso.with(this).load(movieDetail.moviePosterUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_loading_error)
                            .into(posterImageView);

    }

    private void showErrorMessageInUI(){
        // Hide Movie Detail and Progress Bar.
        movieDetailContainer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        // Show an Error message.
        errorOcurredTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBarInUI(){
        // Hide Movie Detail and Progress Bar.
        movieDetailContainer.setVisibility(View.INVISIBLE);
        errorOcurredTextView.setVisibility(View.INVISIBLE);

        // Show an Error message.
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showMovieDetailContainerInUI(){
        // Hide Movie Detail and Progress Bar.
        progressBar.setVisibility(View.INVISIBLE);
        errorOcurredTextView.setVisibility(View.INVISIBLE);

        // Show an Error message.
        movieDetailContainer.setVisibility(View.VISIBLE);
    }

}
