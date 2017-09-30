package com.peterark.popularmovies.popularmovies.models;

// Used to represent each item of the Movie List in the Home Screen.
public class MovieItem{

    private final int movieId;
    private final String moviePosterUrl;

    private MovieItem(int movieId, String moviePosterUrl){
        this.movieId        = movieId;
        this.moviePosterUrl = moviePosterUrl;
    }

    // ---------------------------------
    //  Getters
    // ---------------------------------
    public int movieId(){return this.movieId;}
    public String moviePosterUrl(){return this.moviePosterUrl;}

    // ---------------------------------
    //  Builder
    // ---------------------------------
    public static class Builder {

        private int movieId;
        private String moviePosterUrl;

        public Builder(){
            this.movieId        = 0;
            this.moviePosterUrl = "";
        }

        public Builder withMovieId(int movieId){
            this.movieId = movieId;
            return this;
        }

        public Builder withMoviePosterUrl(String moviePosterUrl){
            this.moviePosterUrl = moviePosterUrl;
            return this;
        }

        public MovieItem build(){
            return new MovieItem(movieId,
                                    moviePosterUrl);
        }
    }
}
