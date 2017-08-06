package com.peterark.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.peterark.popularmovies.popularmovies.Constants;
import com.peterark.popularmovies.popularmovies.utils.MovieHelperUtils;

public class Movie implements Parcelable {

    private final String movieTitle;
    private final String movieReleaseDate;
    private final String movieRating;
    private final String movieSynopsis;
    private final String moviePosterUrl;

    private Movie(String movieTitle, String movieReleaseDate, String movieRating, String movieSynopsis, String moviePosterUrl) {
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRating = movieRating;
        this.movieSynopsis = movieSynopsis;
        this.moviePosterUrl = moviePosterUrl;
    }

    // ---------------------------------
    // Parcelable
    // ---------------------------------
    private Movie(Parcel in){
        this.movieTitle         = in.readString();
        this.movieReleaseDate   = in.readString();
        this.movieRating        = in.readString();
        this.movieSynopsis      = in.readString();
        this.moviePosterUrl     = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieRating);
        parcel.writeString(movieSynopsis);
        parcel.writeString(moviePosterUrl);
    }


    public final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

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

        public Builder(){
            this.movieTitle         = "NO_NAME";
            this.movieReleaseDate   = "";
            this.movieRating        = "";
            this.movieSynopsis      = "";
            this.moviePosterUrl     = "";
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

        public Movie build(){
            return new Movie(movieTitle,
                                movieReleaseDate,
                                movieRating,
                                movieSynopsis,
                                moviePosterUrl);
        }
    }
}
