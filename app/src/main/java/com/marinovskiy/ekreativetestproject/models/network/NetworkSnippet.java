package com.marinovskiy.ekreativetestproject.models.network;

public class NetworkSnippet {

    private String title;

    private String description;

    private NetworkResource resourceId;

    private NetworkThumbnails thumbnails;

    private String playlistId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
