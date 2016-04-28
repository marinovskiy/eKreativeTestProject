package com.marinovskiy.ekreativetestproject.models;

import com.google.gson.annotations.SerializedName;

public class NetworkCover {

    @SerializedName("source")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}