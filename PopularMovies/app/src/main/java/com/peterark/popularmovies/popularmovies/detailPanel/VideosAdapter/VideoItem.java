package com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter;

public class VideoItem {
    public final String videoTitle;
    public final String videoUrlString;
    public final String videoUrlThumbnailUrl;

    public VideoItem(String videoTitle,String videoUrlString,String videoUrlThumbnailUrl){
        this.videoTitle             = videoTitle;
        this.videoUrlString         = videoUrlString;
        this.videoUrlThumbnailUrl   = videoUrlThumbnailUrl;
    }

}
