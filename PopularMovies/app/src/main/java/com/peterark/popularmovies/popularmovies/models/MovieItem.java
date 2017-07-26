package com.peterark.popularmovies.popularmovies.models;

/**
 * Created by PETER on 26/7/2017.
 */

// Used to represent each item of the Movie List in the Home Screen.
public class MovieItem {

    private int movieId;
    private String movieName;
    private String moviePosterUrl;

    private MovieItem(int movieId, String movieName, String moviePosterUrl){
        this.movieId        = movieId;
        this.movieName      = movieName;
        this.moviePosterUrl = moviePosterUrl;
    }

    // ---------------------------------
    //  Getters
    // ---------------------------------
    public int movieId(){return this.movieId;}
    public String movieName(){return this.movieName;}
    public String moviePosterUrl(){return this.moviePosterUrl;}


    // ---------------------------------
    //  Builder
    // ---------------------------------
    public static class Builder {

        private int movieId;
        private String movieName;
        private String moviePosterUrl;

        public Builder(){
            this.movieId        = 0;
            this.movieName      = "";
            this.moviePosterUrl = "";
        }

        public Builder withMovieId(int movieId){
            this.movieId = movieId;
            return this;
        }

        public Builder withMovieName(String movieName){
            this.movieName = movieName;
            return this;
        }

        public Builder withMoviePosterUrl(String moviePosterUrl){
            this.moviePosterUrl = moviePosterUrl;
            return this;
        }

        public MovieItem build(){
            return new MovieItem(movieId,
                                    movieName,
                                    moviePosterUrl);
        }
    }
}
