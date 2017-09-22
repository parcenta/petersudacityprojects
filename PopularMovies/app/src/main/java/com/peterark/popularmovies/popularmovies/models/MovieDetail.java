package com.peterark.popularmovies.popularmovies.models;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter.ReviewItem;
import com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter.VideoItem;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail {

    private final String movieTitle;
    private final String movieReleaseDate;
    private final String movieRating;
    private final String movieSynopsis;
    private final String moviePosterUrl;
    public List<VideoItem> movieVideoList;
    public List<ReviewItem> movieReviewList;

    private MovieDetail(String movieTitle, String movieReleaseDate, String movieRating, String movieSynopsis, String moviePosterUrl, List<VideoItem> videoList, List<ReviewItem> reviewList) {
        this.movieTitle         = movieTitle;
        this.movieReleaseDate   = movieReleaseDate;
        this.movieRating        = movieRating;
        this.movieSynopsis      = movieSynopsis;
        this.moviePosterUrl     = moviePosterUrl;
        this.movieVideoList     = videoList;
        this.movieReviewList    = reviewList;
    }

    // ---------------------------------
    // Setters (Some)
    // ---------------------------------
    public void setMovieVideoList(List<VideoItem> videoList){
        this.movieVideoList = videoList;
    }
    public void setMovieReviewList(List<ReviewItem> reviewList){
        this.movieReviewList = reviewList;
    }

    // ---------------------------------
    // Getters
    // ---------------------------------
    public String movieTitle() {
        return this.movieTitle;
    }
    public String movieReleaseDate() {
        return this.movieReleaseDate;
    }
    public String movieRating() {
        return this.movieRating;
    }
    public String movieSynopsis() {
        return this.movieSynopsis;
    }
    public String moviePosterUrl() {
        return this.moviePosterUrl;
    }
    public List<VideoItem> movieVideoList(){return this.movieVideoList;}
    public List<ReviewItem> movieReviewList(){return this.movieReviewList;}


    // ---------------------------------
    //  Builder
    // ---------------------------------
    public static class Builder {

        private String movieTitle;
        private String movieReleaseDate;
        private String movieRating;
        private String movieSynopsis;
        private String moviePosterUrl;
        private List<VideoItem> movieVideoList;
        private List<ReviewItem> movieReviewList;

        public Builder(){
            this.movieTitle         = "NO_NAME";
            this.movieReleaseDate   = "";
            this.movieRating        = "";
            this.movieSynopsis      = "";
            this.moviePosterUrl     = "";
            this.movieVideoList     = new ArrayList<>();
            this.movieReviewList    = new ArrayList<>();
        }

        public Builder withMovieTitle(String movieTitle){
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withMovieReleaseDate(String movieReleaseDate){
            this.movieReleaseDate = MovieHelperUtils.getDateAsMMMDDYYYYWithMonthName(movieReleaseDate);
            return this;
        }

        public Builder withMovieRating(double movieRating){
            this.movieRating = movieRating + "/" + Constants.MAX_MOVIE_RATING;
            return this;
        }

        public Builder withMovieSynopsis(String movieSynopsis){
            this.movieSynopsis = movieSynopsis != null ? movieSynopsis : "No info.";
            return this;
        }

        public Builder withMoviePosterUrl(String moviePosterUrl){
            this.moviePosterUrl = moviePosterUrl;
            return this;
        }

        public Builder withMovieVideoList(List<VideoItem> videoList){
            this.movieVideoList = videoList;
            return this;
        }

        public Builder withMovieReviewList(List<ReviewItem> reviewList){
            this.movieReviewList = reviewList;
            return this;
        }

        public MovieDetail build(){
            return new MovieDetail(movieTitle,
                                movieReleaseDate,
                                movieRating,
                                movieSynopsis,
                                moviePosterUrl,
                                movieVideoList,
                                movieReviewList);
        }
    }
}
