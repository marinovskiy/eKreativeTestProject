package com.marinovskiy.ekreativetestproject.models;

public class NetworkUser {

    private String id;

    private String name;

    private String email;

    private NetworkCover cover;

    private NetworkPicture picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NetworkCover getCover() {
        return cover;
    }

    public void setCover(NetworkCover cover) {
        this.cover = cover;
    }

    public NetworkPicture getPicture() {
        return picture;
    }

    public void setPicture(NetworkPicture picture) {
        this.picture = picture;
    }
}