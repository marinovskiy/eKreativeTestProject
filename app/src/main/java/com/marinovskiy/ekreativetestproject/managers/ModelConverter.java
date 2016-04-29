package com.marinovskiy.ekreativetestproject.managers;

import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.db.Video;
import com.marinovskiy.ekreativetestproject.models.network.NetworkUser;
import com.marinovskiy.ekreativetestproject.models.network.NetworkVideo;

public class ModelConverter {

    public static User convertToUser(NetworkUser networkUser) {
        return new User(networkUser.getId(),
                networkUser.getName(),
                networkUser.getEmail(),
                networkUser.getPicture().getData().getUrl(),
                networkUser.getCover().getUrl());
    }

    public static Video convertToVideo(NetworkVideo networkVideo) {
        return new Video(networkVideo.getId(),
                networkVideo.getSnippet().getThumbnails().getVideoPicture().getUrl(),
                networkVideo.getSnippet().getVideoTitle(),
                networkVideo.getSnippet().getDescription(),
                networkVideo.getContentDetails().getDuration());
    }

}