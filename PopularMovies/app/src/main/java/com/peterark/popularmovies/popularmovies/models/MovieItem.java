package com.peterark.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

// Used to represent each item of the Movie List in the Home Screen.
public class MovieItem implements Parcelable{

    private final int movieId;
    private final String moviePosterUrl;

    private MovieItem(int movieId, String moviePosterUrl){
        this.movieId        = movieId;
        this.moviePosterUrl = moviePosterUrl;
    }

    // ---------------------------------
    // Parcelable Methods
    // ---------------------------------
    private MovieItem(Parcel in){
        movieId         = in.readInt();
        moviePosterUrl  = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(moviePosterUrl);
    }

    public final Parcelable.Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }
    };

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
