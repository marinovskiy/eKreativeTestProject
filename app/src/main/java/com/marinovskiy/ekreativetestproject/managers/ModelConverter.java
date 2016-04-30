package com.marinovskiy.ekreativetestproject.managers;

import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.db.Video;
import com.marinovskiy.ekreativetestproject.models.network.NetworkUser;
import com.marinovskiy.ekreativetestproject.models.network.NetworkVideo;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public static User convertToUser(NetworkUser networkUser) {
        return new User(networkUser.getId(),
                networkUser.getName(),
                networkUser.getEmail(),
                networkUser.getPicture().getData().getUrl(),
                networkUser.getCover().getUrl());
    }

    public static List<Video> convertToVideos(List<NetworkVideo> networkVideoList, String playlistId) {
        List<Video> videoList = new ArrayList<>();
        for (NetworkVideo networkVideo : networkVideoList) {
            videoList.add(new Video(networkVideo.getId(),
                    networkVideo.getSnippet().getThumbnails().getVideoPicture().getUrl(),
                    networkVideo.getSnippet().getVideoTitle(),
                    networkVideo.getSnippet().getDescription(),
                    networkVideo.getContentDetails().getDuration(),
                    playlistId));
        }
        return videoList;
    }

}