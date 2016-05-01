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
        if (getUser(user.getId()) != null) {
            String updateQuery = "UPDATE users\nSET name = '" + user.getName()
                    + "', email = '" + user.getEmail()
                    + "', avatarUrl = '" + user.getAvatarUrl()
                    + "', coverUrl = '" + user.getCoverUrl()
                    + "'\nWHERE id = '" + user.getId() + "'";
            executeQuery(updateQuery, User.class);
        } else {
            add(user);
        }
    }

    public void saveVideos(List<VideoParcelable> videoParcelableList) {
        for (VideoParcelable video : videoParcelableList) {
            if (getVideo(video.getId()) != null) {
                String updateQuery = "UPDATE videos\nSET title = '" + video.getTitle()
                        + "', pictureUrl = '" + video.getPictureUrl()
                        + "', description = '" + video.getDescription()
                        + "', duration = '" + video.getDuration()
                        + "'\nWHERE id = '" + video.getId() + "'";
                executeQuery(updateQuery, VideoParcelable.class);
            } else {
                add(video);
            }
        }
    }

    public User getUser(String userId) {
        List<User> userList = getAllWhere(String.format("id = '%s'", userId), User.class);
        return userList.size() != 0 ? userList.get(0) : null;
    }

    public VideoParcelable getVideo(String videoId) {
        List<VideoParcelable> videoList = getAllWhere(String.format("id = '%s'", videoId),
                VideoParcelable.class);
        return videoList.size() != 0 ? videoList.get(0) : null;
    }

    public List<VideoParcelable> getPlaylist(String playListId) {
        List<VideoParcelable> videoParcelableList = getAllWhere(
                String.format("playlistId = '%s'", playListId), VideoParcelable.class);
        return videoParcelableList.size() != 0 ? videoParcelableList : null;
    }

}