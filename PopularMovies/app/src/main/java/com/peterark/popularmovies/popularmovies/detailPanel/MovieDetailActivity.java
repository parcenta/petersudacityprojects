package com.peterark.popularmovies.popularmovies.detailPanel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ActivityMovieDetailBinding;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewItem;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewsAdapter;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideoItem;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideosAdapter;
import com.peterark.popularmovies.popularmovies.models.MovieDetail;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements VideosAdapter.OnVideoClickHandler{

    private final String TAG = this.getClass().getSimpleName();

    // Intent Extras names
    private final static String MOVIE_ID = "MOVIE_ID";

    // Intent variables
    private static int mMovieId;

    // Movie
    private MovieDetail movieDetailDetail;

    // For Saved Instance
    private static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    // Asynctask
    private LoadMovieDetailTask loadMovieDetailTask;

    // Adapters
    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

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

        // Setting Video(Trailers,etc) Adapter
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mBinding.videosRecyclerView.setLayoutManager(horizontalLayoutManager);
        mBinding.videosRecyclerView.setHasFixedSize(true);
        mVideosAdapter = new VideosAdapter(this,this);
        mBinding.videosRecyclerView.setAdapter(mVideosAdapter);

        // Setting Reviews Adapter
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mBinding.reviewsRecyclerView.setLayoutManager(verticalLayoutManager);
        mBinding.reviewsRecyclerView.setHasFixedSize(true);
        mReviewsAdapter = new ReviewsAdapter(this);
        mBinding.reviewsRecyclerView.setAdapter(mReviewsAdapter);

        // Load Data in Activity
        if(savedInstanceState!=null && savedInstanceState.containsKey(MOVIE_DETAIL)){
            movieDetailDetail = savedInstanceState.getParcelable(MOVIE_DETAIL);
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

    @Override
    public void onMovieClick(VideoItem item) {
        if (item!=null){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.videoUrlString)));
        }
    }

    // --------------------------------------------------------
    //  Loading Movies AsyncTask
    // --------------------------------------------------------
    private class LoadMovieDetailTask extends AsyncTask<Void,Void,MovieDetail> {

        @Override
        protected void onPreExecute() {
            showProgressBarInUI();
        }

        @Override
        protected MovieDetail doInBackground(Void... params) {

            // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
            List<String> parametersList = new ArrayList<>();
            parametersList.add(mMovieId + "");
            URL movieDetailRequestUrl           = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_BY_ID,parametersList);
            URL movieDetailVideosRequestUrl     = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_VIDEOS_BY_ID,parametersList);
            URL movieDetailReviewsRequestUrl    = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_REVIEWS_BY_ID,parametersList);

            try {

                // ---------------------------------------------
                // GET MOVIE DETAIL BASIC INFO
                // ---------------------------------------------

                String response = NetworkUtils
                        .getResponseFromHttpUrl(MovieDetailActivity.this, movieDetailRequestUrl);

                if(response==null)
                    return null;

                // Get the Basic Info of the Movie from the first webservice.
                MovieDetail movieDetail = MovieHelperUtils.getMovieDetailFromJson(response);

                // ---------------------------------------------
                // GET MOVIE DETAIL VIDEO LIST
                // ---------------------------------------------
                String responseMovieList = NetworkUtils
                        .getResponseFromHttpUrl(MovieDetailActivity.this, movieDetailVideosRequestUrl);
                movieDetail.setMovieVideoList(MovieHelperUtils.getMovieDetailVideoListFromJson(responseMovieList));

                // ---------------------------------------------
                // GET MOVIE DETAIL BASIC INFO
                // ---------------------------------------------
                String responseReviewList = NetworkUtils
                        .getResponseFromHttpUrl(MovieDetailActivity.this, movieDetailReviewsRequestUrl);
                movieDetail.setMovieReviewList(MovieHelperUtils.getMovieDetailReviewsListFromJson(responseReviewList));

                return movieDetail;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetailResponse){

            // Set the Adapter List.
            movieDetailDetail = movieDetailResponse;

            if(movieDetailDetail !=null){
                showMovieDetailContainerInUI();
                refreshMovieUI();
            }else
                showErrorMessageInUI();

        }
    }


    private void refreshMovieUI(){

        if(movieDetailDetail ==null) {
            showErrorMessageInUI();
            return;
        }

        mBinding.movieTitleTextView.setText(movieDetailDetail.movieTitle());
        mBinding.movieReleaseDateTextView.setText(movieDetailDetail.movieReleaseDate());
        mBinding.movieRatingTextView.setText(movieDetailDetail.movieRating());
        mBinding.movieSynopsisTextView.setText(movieDetailDetail.movieSynopsis());
        Picasso.with(this).load(movieDetailDetail.moviePosterUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_loading_error)
                            .into(mBinding.posterHolder);
        mVideosAdapter.setItemList(movieDetailDetail.movieVideoList());
        mReviewsAdapter.setItemList(movieDetailDetail.movieReviewList());

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
}
