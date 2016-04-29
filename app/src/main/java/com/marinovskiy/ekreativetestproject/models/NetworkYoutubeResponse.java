package com.marinovskiy.ekreativetestproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkYoutubeResponse {

    @SerializedName("items")
    private List<NetworkVideo> videoList;

    public List<NetworkVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<NetworkVideo> videoList) {
        this.videoList = videoList;
    }
}
