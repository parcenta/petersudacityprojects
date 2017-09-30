package com.peterark.popularmovies.popularmovies.models;

public class MovieDetail {

    private final String movieTitle;
    private final String movieReleaseDate;
    private final String movieRating;
    private final String movieSynopsis;
    private final String moviePosterPath;
    private boolean movieIsFavorite;

    private MovieDetail(String movieTitle,
                        String movieReleaseDate,
                        String movieRating,
                        String movieSynopsis,
                        String moviePosterPath,
                        boolean movieIsFavorite) {
        this.movieTitle         = movieTitle;
        this.movieReleaseDate   = movieReleaseDate;
        this.movieRating        = movieRating;
        this.movieSynopsis      = movieSynopsis;
        this.moviePosterPath    = moviePosterPath;
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
    public String moviePosterPath() {
        return this.moviePosterPath;
    }
    public boolean movieIsFavorite() {return this.movieIsFavorite;}

    // ---------------------------------
    //  Builder
    // ---------------------------------
    public static class Builder {

        private String movieTitle;
        private String movieReleaseDate;
        private String movieRating;
        private String movieSynopsis;
        private String moviePosterPath;
        private boolean movieIsFavorite;

        public Builder(){
            this.movieTitle         = "NO_NAME";
            this.movieReleaseDate   = "0";
            this.movieRating        = "0.0";
            this.movieSynopsis      = "";
            this.moviePosterPath    = "";
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

        public Builder withMoviePosterPath(String moviePosterPath){
            this.moviePosterPath = moviePosterPath;
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
                                moviePosterPath,
                                movieIsFavorite);
        }
    }
}
