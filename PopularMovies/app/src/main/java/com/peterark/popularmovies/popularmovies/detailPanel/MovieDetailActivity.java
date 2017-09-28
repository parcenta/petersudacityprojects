package com.peterark.popularmovies.popularmovies.detailPanel;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.database.contracts.FavoriteMoviesContract;
import com.peterark.popularmovies.popularmovies.databinding.ActivityMovieDetailBinding;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewItem;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewsAdapter;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideoItem;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideosAdapter;
import com.peterark.popularmovies.popularmovies.models.MovieDetail;
import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;
import com.peterark.popularmovies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity
                                    implements VideosAdapter.OnVideoClickHandler{

    private final String TAG = this.getClass().getSimpleName();

    // Intent Extras names
    private final static String MOVIE_ID = "MOVIE_ID";

    // Loader ID
    private final static int LOADER_ID_FOR_MOVIE_DETAIL_BASIC_INFO_LOAD = 1001;
    private final static int LOADER_ID_FOR_MOVIE_DETAIL_VIDEOS_LOAD     = 1002;
    private final static int LOADER_ID_FOR_MOVIE_DETAIL_REVIEWS_LOAD    = 1003;
    private final static int LOADER_ID_FOR_MOVIE_MARK_AS_FAVORITE       = 2000;

    // Response Codes
    private final static int MARK_AS_FAVORITE_SUCCESSFULL       = 4000;
    private final static int MARK_AS_FAVORITE_ERROR             = 4001;
    private final static int MARK_AS_NOT_FAVORITE_SUCCESSFULL   = 4002;
    private final static int MARK_AS_NOT_FAVORITE_ERROR         = 4003;



    // Intent variables
    private static int mMovieId;

    // Movie
    private MovieDetail movieDetailDetail;

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
                getSupportLoaderManager().restartLoader(LOADER_ID_FOR_MOVIE_DETAIL_BASIC_INFO_LOAD,null,movieDetailResultLoaderListener);
            }
        });

        // Set Mark as favorite Action.
        mBinding.markAsFavoriteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().restartLoader(LOADER_ID_FOR_MOVIE_MARK_AS_FAVORITE,null,movieMarkAsFavoriteResultLoaderListener);
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

        // Resume/Start
        getSupportLoaderManager().initLoader(LOADER_ID_FOR_MOVIE_DETAIL_BASIC_INFO_LOAD,null,movieDetailResultLoaderListener);
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
    public void onVideoClick(VideoItem item) {
        if (item!=null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.videoUrlString)));
    }

    private void refreshMovieUI(){

        if(movieDetailDetail ==null) {
            showErrorMessageInUI();
            return;
        }

        mBinding.movieTitleTextView.setText(movieDetailDetail.movieTitle());
        mBinding.movieReleaseDateTextView.setText(MovieHelperUtils.getDateAsMMMDDYYYYWithMonthName(movieDetailDetail.movieReleaseDate()));
        mBinding.movieRatingTextView.setText(movieDetailDetail.movieRating()  + "/" + Constants.MAX_MOVIE_RATING);
        mBinding.movieSynopsisTextView.setText(movieDetailDetail.movieSynopsis());
        mBinding.markAsFavoriteAction.setImageResource(movieDetailDetail.movieIsFavorite ? R.drawable.ic_favorite_activated_white : R.drawable.ic_favorite_deactivated_white);
        Picasso.with(this).load(movieDetailDetail.moviePosterUrl())
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



    /* -----------------------------------------------------------------------------------------------------------------
     * "Movie Detail Basic Info" Loader Callback
     -------------------------------------------------------------------------------------------------------------------*/
    // Based on the forum https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
    private LoaderManager.LoaderCallbacks<MovieDetail> movieDetailResultLoaderListener
            = new LoaderManager.LoaderCallbacks<MovieDetail>() {

        @Override
        public Loader<MovieDetail> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<MovieDetail>(MovieDetailActivity.this) {

                MovieDetail cachedMovieDetail;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    showProgressBarInUI();
                    if(cachedMovieDetail!=null)
                        deliverResult(cachedMovieDetail);
                    else
                        forceLoad();
                }

                @Override
                public MovieDetail loadInBackground() {

                    Context context = getContext();

                    try {

                        // Init MovieDetail variables (that will contain all the info for the UI and other stuff).
                        MovieDetail movieDetail;

                        // ---------------------------------------------
                        // GET MOVIE DETAIL BASIC INFO
                        // ---------------------------------------------

                        // First Check if the selected movie is saved in the Db (via Favorite mode).
                        Uri uriFavoriteMovieWithId = FavoriteMoviesContract.FavoritesMoviesEntry.buildUriForFavoriteMovieWithId(mMovieId);
                        Cursor favoriteMovieCursor = context.getContentResolver().query(uriFavoriteMovieWithId,null,null,null,null,null);

                        // IF MOVIE IS FAVORITE: If FavoriteMovie cursor has results, then the data of the movie is saved in the db.
                        if (favoriteMovieCursor != null && favoriteMovieCursor.moveToNext()) {
                            Log.d(TAG, "loadInBackground: Movie is a favorite one. So we take the info from the Database.");
                            movieDetail = MovieHelperUtils.getMovieDetailFromCursor(favoriteMovieCursor);
                        }
                        else // MOVIE IS NOT FAVORITE: If it isnt tagged as favorite then get the movie from the webservice.
                        {
                            Log.d(TAG, "loadInBackground: Movie is NOT a favorite one. So we take the info from the webservice.");

                            // Get the Url depending in the request mode (movies ordered by most_popular or top_rated).
                            List<String> parametersList = new ArrayList<>();
                            parametersList.add(String.valueOf(mMovieId));
                            URL movieDetailRequestUrl           = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_BY_ID,parametersList);

                            String response = NetworkUtils.getResponseFromHttpUrl(context, movieDetailRequestUrl);
                            if(response==null)
                                return null;

                            // Get the Basic Info of the Movie from json response.
                            movieDetail = MovieHelperUtils.getMovieDetailFromJson(response);
                        }

                        return movieDetail;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<MovieDetail> loader, MovieDetail data) {

            Log.d(TAG, "MOVIEDETAIL onLoadFinished is called.");

            // Set the Movie Detail
            movieDetailDetail = data;

            if (movieDetailDetail != null) {

                // Once we succesfully have the Movie Detail
                getSupportLoaderManager().initLoader(LOADER_ID_FOR_MOVIE_DETAIL_VIDEOS_LOAD,null,movieDetailVideosLoaderListener);
                getSupportLoaderManager().initLoader(LOADER_ID_FOR_MOVIE_DETAIL_REVIEWS_LOAD,null,movieDetailReviewsLoaderListener);
                showMovieDetailContainerInUI();
                refreshMovieUI();
            } else
                showErrorMessageInUI();
        }

        @Override
        public void onLoaderReset(Loader<MovieDetail> loader) {

        }
    };



    /* -----------------------------------------------------------------------------------------------------------------
     * "Videos (Trailers, Teasers, etc)" Loader Callback
     -------------------------------------------------------------------------------------------------------------------*/
    private LoaderManager.LoaderCallbacks<List<VideoItem>> movieDetailVideosLoaderListener
            = new LoaderManager.LoaderCallbacks<List<VideoItem>>(){

        @Override
        public Loader<List<VideoItem>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<VideoItem>>(MovieDetailActivity.this){

                List<VideoItem> cachedVideoItemList;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    mBinding.videosRecyclerView.setVisibility(View.GONE);                   // HIDE
                    mBinding.noVideosFoundMessage.setVisibility(View.GONE);                 // HIDE
                    mBinding.errorOccurredLoadingVideosTextview.setVisibility(View.GONE);   // HIDE
                    mBinding.videosLoadingProgressBar.setVisibility(View.VISIBLE);          // SHOW

                    if(cachedVideoItemList!=null)
                        deliverResult(cachedVideoItemList);
                    else
                        forceLoad();
                }

                @Override
                public List<VideoItem> loadInBackground() {

                    // Get the Url for loading the video list of the movie.
                    List<String> parametersList = new ArrayList<>();
                    parametersList.add(String.valueOf(mMovieId));
                    URL movieDetailVideosRequestUrl     = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_VIDEOS_BY_ID,parametersList);

                    // Calling Videos WS...
                    try {
                        String responseReviewList = NetworkUtils.getResponseFromHttpUrl(getContext(), movieDetailVideosRequestUrl);
                        if(responseReviewList==null)
                            return null;

                        return MovieHelperUtils.getMovieDetailVideoListFromJson(responseReviewList);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(List<VideoItem> videoList) {
                    cachedVideoItemList = videoList;
                    super.deliverResult(cachedVideoItemList);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<VideoItem>> loader, List<VideoItem> itemList) {

            mBinding.videosLoadingProgressBar.setVisibility(View.GONE);

            if (itemList!=null){
                if(itemList.size()>0) {
                    mVideosAdapter.setItemList(itemList);
                    mBinding.videosRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    mReviewsAdapter.setItemList(null);
                    mBinding.noVideosFoundMessage.setVisibility(View.VISIBLE);
                }
            }else
                mBinding.errorOccurredLoadingVideosTextview.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoaderReset(Loader<List<VideoItem>> loader) {

        }
    };

    /* -----------------------------------------------------------------------------------------------------------------
     * "Reviews" Loader Callback
     -------------------------------------------------------------------------------------------------------------------*/
    private LoaderManager.LoaderCallbacks<List<ReviewItem>> movieDetailReviewsLoaderListener
            = new LoaderManager.LoaderCallbacks<List<ReviewItem>>(){

        @Override
        public Loader<List<ReviewItem>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<ReviewItem>>(MovieDetailActivity.this){

                List<ReviewItem> cachedReviewItemList;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    mBinding.reviewsRecyclerView.setVisibility(View.GONE);                  // HIDE
                    mBinding.noReviewsFoundMessage.setVisibility(View.GONE);                // HIDE
                    mBinding.errorOccurredLoadingReviewsTextview.setVisibility(View.GONE);  // HIDE
                    mBinding.reviewLoadingProgressBar.setVisibility(View.VISIBLE);          // SHOW

                    if(cachedReviewItemList!=null)
                        deliverResult(cachedReviewItemList);
                    else {
                        forceLoad();
                    }
                }

                @Override
                public List<ReviewItem> loadInBackground() {

                    // Get the Url for loading the video list of the movie.
                    List<String> parametersList = new ArrayList<>();
                    parametersList.add(String.valueOf(mMovieId));
                    URL movieDetailVideosRequestUrl     = NetworkUtils.buildUrl(Constants.SEARCH_MOVIE_DETAIL_REVIEWS_BY_ID,parametersList);

                    // Calling Reviews WS...
                    try {
                        String responseReviewList = NetworkUtils.getResponseFromHttpUrl(getContext(), movieDetailVideosRequestUrl);
                        if(responseReviewList==null)
                            return null;

                        return MovieHelperUtils.getMovieDetailReviewsListFromJson(responseReviewList);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }


                @Override
                public void deliverResult(List<ReviewItem> reviewList) {
                    cachedReviewItemList = reviewList;
                    super.deliverResult(cachedReviewItemList);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<ReviewItem>> loader, List<ReviewItem> itemList) {
            mBinding.reviewLoadingProgressBar.setVisibility(View.GONE);

            if (itemList!=null){
                if(itemList.size()>0) {
                    mReviewsAdapter.setItemList(itemList);
                    mBinding.reviewsRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    mReviewsAdapter.setItemList(null);
                    mBinding.noReviewsFoundMessage.setVisibility(View.VISIBLE);
                }
            }else
                mBinding.errorOccurredLoadingReviewsTextview.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoaderReset(Loader<List<ReviewItem>> loader) {

        }
    };


    /* -----------------------------------------------------------------------------------------------------------------
     * "Mark as Favorite" Loader Callback
     -------------------------------------------------------------------------------------------------------------------*/
    private LoaderManager.LoaderCallbacks<Integer> movieMarkAsFavoriteResultLoaderListener
            = new LoaderManager.LoaderCallbacks<Integer>() {


        @Override
        public Loader<Integer> onCreateLoader(int id, final Bundle args) {

            return new AsyncTaskLoader<Integer>(MovieDetailActivity.this) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public Integer loadInBackground() {

                    // First check if the movie is in the Favorite Table.
                    boolean movieIsFavorite = false;
                    Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoritesMoviesEntry.buildUriForFavoriteMovieWithId(mMovieId),null,null,null,null,null);
                    if(cursor!=null) {
                        movieIsFavorite = cursor.getCount() > 0;
                        cursor.close();
                    }



                    // If movie is favorite, then delete it from the FavoriteMovies table...
                    if (movieIsFavorite) {
                        Log.d(TAG, "loadInBackground: Movie is already favorite, then we delete it.");
                        return getContentResolver().delete(FavoriteMoviesContract.FavoritesMoviesEntry.buildUriForFavoriteMovieWithId(mMovieId), null, null) > 0 ? MARK_AS_NOT_FAVORITE_SUCCESSFULL : MARK_AS_NOT_FAVORITE_ERROR;
                    }else // If movie is NOT favorite, then save it in the FavoriteMovies table.
                    {

                        Log.d(TAG, "loadInBackground: Movie is not a favorite, then we mark it as favorite.");
                        ContentValues values = new ContentValues();
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_ID,mMovieId);
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_NAME,movieDetailDetail.movieTitle());
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_SYNOPSIS,movieDetailDetail.movieSynopsis());
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_USER_RATING,Double.valueOf(movieDetailDetail.movieRating()));
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_RELEASE_DATE,Integer.valueOf(movieDetailDetail.movieReleaseDate()));
                        values.put(FavoriteMoviesContract.FavoritesMoviesEntry.COLUMN_MOVIE_POSTER_URL,"google.com");

                        Uri returnUri = getContentResolver().insert(FavoriteMoviesContract.FavoritesMoviesEntry.CONTENT_URI, values);
                        return returnUri != null ? MARK_AS_FAVORITE_SUCCESSFULL : MARK_AS_FAVORITE_ERROR;

                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer responseCode) {
            switch (responseCode){
                case MARK_AS_FAVORITE_SUCCESSFULL:
                    mBinding.markAsFavoriteAction.setImageResource(R.drawable.ic_favorite_activated_white);
                    break;
                case MARK_AS_NOT_FAVORITE_SUCCESSFULL:
                    mBinding.markAsFavoriteAction.setImageResource(R.drawable.ic_favorite_deactivated_white);
                    break;
                default:
                    Toast.makeText(MovieDetailActivity.this,"An error has ocurred when marking the movie as favorite",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {
        }
    };

}
