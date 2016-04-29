package com.marinovskiy.ekreativetestproject.models.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkYoutubeResponse {

    private String nextPageToken;

    private NetworkPageInfo pageInfo;

    @SerializedName("items")
    private List<NetworkVideo> videoList;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public NetworkPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(NetworkPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<NetworkVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<NetworkVideo> videoList) {
        this.videoList = videoList;
    }
}