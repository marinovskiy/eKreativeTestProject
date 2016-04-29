package com.marinovskiy.ekreativetestproject.models;

public class NetworkVideo {

    private String id;

    private NetworkSnippet snippet;

    private NetworkContentDetails contentDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NetworkSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(NetworkSnippet snippet) {
        this.snippet = snippet;
    }

    public NetworkContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(NetworkContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }
}
