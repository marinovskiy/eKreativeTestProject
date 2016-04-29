package com.marinovskiy.ekreativetestproject.models;

import com.google.gson.annotations.SerializedName;

public class NetworkThumbnails {

    @SerializedName("medium")
    private NetworkVideoPicture videoPicture;

    public NetworkVideoPicture getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(NetworkVideoPicture videoPicture) {
        this.videoPicture = videoPicture;
    }
}
