package com.marinovskiy.ekreativetestproject.models;

public class NetworkVideo {

    private String id;

    private NetworkSnippet snippet;

    private NetworkContentDetails contentDetails;

    private NetworkStatistics statistics;

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

    public NetworkStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(NetworkStatistics statistics) {
        this.statistics = statistics;
    }
}
