package com.marinovskiy.ekreativetestproject.db;

import android.content.Context;

import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.db.Video;
import com.rightutils.rightutils.db.RightDBUtils;

import java.util.List;

public class DbUtils extends RightDBUtils {

    private static DbUtils sDbUtils;

    public static DbUtils newInstance(Context context, String name, int version) {
        sDbUtils = new DbUtils();
        sDbUtils.setDBContext(context, name, version);
        return sDbUtils;
    }

    public static void saveUser(User user) {
        String userId = user.getId();
        if (getUser(userId) != null) {
            sDbUtils.deleteWhere(User.class, String.format("id = '%s'", userId));
        }
        sDbUtils.add(user);
    }

    public static void saveVideos(List<Video> videoList) {
        String playlistId = videoList.get(0).getPlaylistId();
        if (getVideos(playlistId) != null) {
            sDbUtils.deleteWhere(Video.class, String.format("playlistId = '%s'", playlistId));
        }
        for (Video video : videoList) {
            sDbUtils.add(video);
        }
    }

    public static User getUser(String userId) {
        List<User> userList = sDbUtils.getAllWhere(String.format("id = '%s'", userId), User.class);
        return userList.size() != 0 ? userList.get(0) : null;
    }

    public static List<Video> getVideos(String playListId) {
        List<Video> videoList = sDbUtils.getAllWhere(String.format("playlistId = '%s'", playListId),
                Video.class);
        return videoList.size() != 0 ? videoList : null;
    }

}