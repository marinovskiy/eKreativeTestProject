package com.marinovskiy.ekreativetestproject.models.network;

import com.google.gson.annotations.SerializedName;

public class NetworkSnippet {

    @SerializedName("publishedAt")
    private String publishedDate;

    @SerializedName("title")
    private String videoTitle;

    private String description;

    private String channelTitle;

    private NetworkResource resourceId;

    private NetworkThumbnails thumbnails;

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public NetworkResource getResourceId() {
        return resourceId;
    }

    public void setResourceId(NetworkResource resourceId) {
        this.resourceId = resourceId;
    }

    public NetworkThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(NetworkThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }
}
