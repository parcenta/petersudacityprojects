package com.peterark.popularmovies.popularmovies.detailPanel;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ActivityMovieDetailBinding;
import com.peterark.popularmovies.popularmovies.models.Movie;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    // Intent Extras names
    private final static String MOVIE_ID = "MOVIE_ID";

    // Intent variables
    private static int mMovieId;

    // Movie
    private Movie movieDetail;

    // For Saved Instance
    private static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    // Asynctask
    private LoadMovieDetailTask loadMovieDetailTask;

    // Binding
    private ActivityMovieDetailBinding mBinding;

    /* -----------------------------------------------------------------
     * Launch Helper
     * -----------------------------------------------------------------*/
    public static void launch(Context context, int movieId) {
        context.startActivity(launchIntent(context, movieId));
    }

    private static Intent launchIntent(Context context, int movieId) {

        Class destinationActivity = MovieDetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra(MOVIE_ID, movieId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Binding
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_detail);

        // Set the Action Bar.
        setupActionBar();

        // Get Intent parameters variables
        getParameters();

        // Set Try again action when movie is not loaded.
        mBinding.errorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieDetail();
            }
        });

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

    private void setupActionBar() {
        // This is just to show a Back Button in the Action bar.
        // I used this in previous personal project, because Im afraid im not an expert in Toolbars yet. =)
        ActionBar ab = getSupportActionBar();
        if(ab!=null)
            ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------------------------
    //  Loading Movies AsyncTask
    // --------------------------------------------------------
    private class LoadMovieDetailTask extends AsyncTask<Void,Void,Movie> {

        @Override
        protected void onPreExecute() {
            showProgressBarInUI();
        }

        @Override
        protected Movie doInBackground(Void... params) {

            // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
            List<String> parametersList = new ArrayList<>();
            parametersList.add(mMovieId + "");
            URL weatherRequestUrl = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_BY_ID,parametersList);

            try {
                String response = NetworkUtils
                        .getResponseFromHttpUrl(MovieDetailActivity.this, weatherRequestUrl);

                if(response==null)
                    return null;

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

        mBinding.movieTitleTextView.setText(movieDetail.movieTitle());
        mBinding.movieReleaseDateTextView.setText(movieDetail.movieReleaseDate());
        mBinding.movieRatingTextView.setText(movieDetail.movieRating());
        mBinding.movieSynopsisTextView.setText(movieDetail.movieSynopsis());
        Picasso.with(this).load(movieDetail.moviePosterUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_loading_error)
                            .into(mBinding.posterHolder);

    }


    private void showErrorMessageInUI(){
        // Hide Movie Detail and Progress Bar.
        mBinding.movieDetailContainer.setVisibility(View.INVISIBLE);
        mBinding.loadingMovieDetailProgressBar.setVisibility(View.INVISIBLE);

        // Show an Error message.
        mBinding.errorTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBarInUI(){
        // Hide Movie Detail and Progress Bar.
        mBinding.movieDetailContainer.setVisibility(View.INVISIBLE);
        mBinding.errorTextView.setVisibility(View.INVISIBLE);

        // Show an Error message.
        mBinding.loadingMovieDetailProgressBar.setVisibility(View.VISIBLE);
    }

    private void showMovieDetailContainerInUI(){
        // Hide Movie Detail and Progress Bar.
        mBinding.loadingMovieDetailProgressBar.setVisibility(View.INVISIBLE);
        mBinding.errorTextView.setVisibility(View.INVISIBLE);

        // Show an Error message.
        mBinding.movieDetailContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // If MovieDetail is not loaded yet, then avoid it to save it in the SaveInstance while screen rotates.
        // This will make that the if-clause (with savedInstanceState) in the OnCreate(), to force to call again the WS.
        if(movieDetail==null) {
            Log.d(TAG,"Avoiding saving moviesList in savedInstanceState...");
            super.onSaveInstanceState(outState);
            return;
        }

        outState.putParcelable(MOVIE_DETAIL,movieDetail);
        super.onSaveInstanceState(outState);
    }
}
