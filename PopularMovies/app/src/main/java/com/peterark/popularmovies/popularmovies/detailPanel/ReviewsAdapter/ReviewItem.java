package com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter;

public class ReviewItem {
    public final String userName;
    public final String reviewCommentary;
    public final String userNameFirstLetter;
    public ReviewItem(String userName, String reviewCommentary){
        this.userName = userName;
        this.reviewCommentary = reviewCommentary;
        this.userNameFirstLetter = (userName!=null && userName.length()>0) ? userName.substring(0,1).toUpperCase() : "0";
    }
}
