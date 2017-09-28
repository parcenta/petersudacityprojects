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
    public boolean movieIsFavorite;

    private MovieDetail(String movieTitle,
                        String movieReleaseDate,
                        String movieRating,
                        String movieSynopsis,
                        String moviePosterUrl,
                        boolean movieIsFavorite) {
        this.movieTitle         = movieTitle;
        this.movieReleaseDate   = movieReleaseDate;
        this.movieRating        = movieRating;
        this.movieSynopsis      = movieSynopsis;
        this.moviePosterUrl     = moviePosterUrl;
        this.movieIsFavorite    = movieIsFavorite;
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

    // ---------------------------------
    //  Builder
    // ---------------------------------
    public static class Builder {

        private String movieTitle;
        private String movieReleaseDate;
        private String movieRating;
        private String movieSynopsis;
        private String moviePosterUrl;
        private boolean movieIsFavorite;

        public Builder(){
            this.movieTitle         = "NO_NAME";
            this.movieReleaseDate   = "0";
            this.movieRating        = "0.0";
            this.movieSynopsis      = "";
            this.moviePosterUrl     = "";
            this.movieIsFavorite    = false;
        }

        public Builder withMovieTitle(String movieTitle){
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withMovieReleaseDate(String movieReleaseDate){
            this.movieReleaseDate = movieReleaseDate;
            return this;
        }

        public Builder withMovieRating(String movieRating){
            this.movieRating = movieRating;
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

        public Builder withMovieIsFavorite(boolean movieIsFavorite){
            this.movieIsFavorite = movieIsFavorite;
            return this;
        }

        public MovieDetail build(){
            return new MovieDetail(movieTitle,
                                movieReleaseDate,
                                movieRating,
                                movieSynopsis,
                                moviePosterUrl,
                                movieIsFavorite);
        }
    }
}
