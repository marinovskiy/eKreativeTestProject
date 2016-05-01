package com.marinovskiy.ekreativetestproject.db;

import android.content.Context;
import android.database.DatabaseUtils;

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
            String userName = DatabaseUtils.sqlEscapeString(user.getName());
            StringBuilder updateQuery = new StringBuilder();
            updateQuery.append("UPDATE users\nSET name = ").append(userName)
                    .append(", email = '").append(user.getEmail())
                    .append("', avatarUrl = '").append(user.getAvatarUrl())
                    .append("', coverUrl = '").append(user.getCoverUrl())
                    .append("'\nWHERE id = '").append(user.getId()).append("';");
            executeQuery(updateQuery.toString(), User.class);
        } else {
            add(user);
        }
    }

    public void saveVideos(List<VideoParcelable> videoParcelableList) {
        for (VideoParcelable video : videoParcelableList) {
            if (getVideo(video.getId()) != null) {
                String title = DatabaseUtils.sqlEscapeString(video.getTitle());
                String description = DatabaseUtils.sqlEscapeString(video.getDescription());
                StringBuilder updateQuery = new StringBuilder();
                updateQuery.append("UPDATE videos\nSET title = ").append(title)
                        .append(", pictureUrl = '").append(video.getPictureUrl())
                        .append("', description = ").append(description)
                        .append(", duration = '").append(video.getDuration())
                        .append("'\nWHERE id = '").append(video.getId()).append("';");
                executeQuery(updateQuery.toString(), VideoParcelable.class);
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