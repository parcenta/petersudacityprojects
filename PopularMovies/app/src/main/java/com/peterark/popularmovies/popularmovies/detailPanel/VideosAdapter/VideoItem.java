package com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter;

/**
 * Created by PETER on 21/9/2017.
 */

public class VideoItem {
    public String videoTitle;
    public String videoUrlString;
    public String videoUrlThumbnailUrl;

    public VideoItem(String videoTitle,String videoUrlString,String videoUrlThumbnailUrl){
        this.videoTitle             = videoTitle;
        this.videoUrlString         = videoUrlString;
        this.videoUrlThumbnailUrl   = videoUrlThumbnailUrl;
    }

}
