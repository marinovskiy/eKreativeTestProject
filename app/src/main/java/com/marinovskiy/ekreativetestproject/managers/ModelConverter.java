package com.marinovskiy.ekreativetestproject.managers;

import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.db.VideoParcelable;
import com.marinovskiy.ekreativetestproject.models.network.NetworkUser;
import com.marinovskiy.ekreativetestproject.models.network.NetworkVideo;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public static User convertToUser(NetworkUser networkUser) {
        User user = new User();
        user.setId(networkUser.getId());
        user.setName(networkUser.getName());
        user.setEmail(networkUser.getEmail() != null ? networkUser.getEmail() : "");
        user.setAvatarUrl(networkUser.getPicture() != null
                ? networkUser.getPicture().getData().getUrl() : "");
        user.setCoverUrl(networkUser.getCover() != null ? networkUser.getCover().getUrl() : "");
        return user;
    }

    public static List<VideoParcelable> convertToVideos(List<NetworkVideo> networkVideoList,
                                                        String playlistId) {
        List<VideoParcelable> videoParcelableList = new ArrayList<>();
        for (NetworkVideo networkVideo : networkVideoList) {
            videoParcelableList.add(new VideoParcelable(networkVideo.getId(),
                    playlistId,
                    networkVideo.getSnippet().getTitle(),
                    networkVideo.getSnippet().getThumbnails().getVideoPicture().getUrl(),
                    networkVideo.getSnippet().getDescription(),
                    networkVideo.getContentDetails().getDuration()));
        }
        return videoParcelableList;
    }

}