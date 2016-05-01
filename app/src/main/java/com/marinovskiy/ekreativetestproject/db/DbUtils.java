package com.marinovskiy.ekreativetestproject.db;

import android.content.Context;

import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.db.VideoParcelable;
import com.rightutils.rightutils.db.RightDBUtils;

import java.util.List;

public class DbUtils extends RightDBUtils {

    public static DbUtils newInstance(Context context, String name, int version) {
        DbUtils dbUtils = new DbUtils();
        dbUtils.setDBContext(context, name, version);
        return dbUtils;
    }

    public void saveUser(User user) {
        String userId = user.getId();
        deleteWhere(User.class, String.format("id = '%s'", userId));
        add(user);
    }

    public void saveVideos(List<VideoParcelable> videoParcelableList) {
        for (VideoParcelable videoParcelable : videoParcelableList) {
            deleteWhere(VideoParcelable.class, String.format("id = '%s'",
                    videoParcelable.getId()));
            add(videoParcelable);
        }
    }

    public User getUser(String userId) {
        List<User> userList = getAllWhere(String.format("id = '%s'", userId), User.class);
        return userList.size() != 0 ? userList.get(0) : null;
    }

    public List<VideoParcelable> getVideos(String playListId) {
        List<VideoParcelable> videoParcelableList = getAllWhere(
                String.format("playlistId = '%s'", playListId), VideoParcelable.class);
        return videoParcelableList.size() != 0 ? videoParcelableList : null;
    }

}