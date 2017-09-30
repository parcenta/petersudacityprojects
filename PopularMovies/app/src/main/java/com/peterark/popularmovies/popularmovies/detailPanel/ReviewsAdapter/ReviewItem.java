package com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter;

/**
 * Created by PETER on 21/9/2017.
 */

public class ReviewItem {
    public String userName;
    public String reviewCommentary;
    public String userNameFirstLetter;
    public ReviewItem(String userName, String reviewCommentary){
        this.userName = userName;
        this.reviewCommentary = reviewCommentary;
        this.userNameFirstLetter = (userName!=null && userName.length()>0) ? userName.substring(0,1).toUpperCase() : "0";
    }
}
