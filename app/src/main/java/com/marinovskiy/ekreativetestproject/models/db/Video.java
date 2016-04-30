package com.marinovskiy.ekreativetestproject.models.db;

import com.rightutils.rightutils.db.TableName;

@TableName("videos")
public class Video {

    private String id;

    private String pictureUrl;

    private String title;

    private String description;

    private String duration;

    private String playlistId;

    public Video() {
    }

    public Video(String id, String pictureUrl, String title, String description, String duration, String playlistId) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.playlistId = playlistId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
