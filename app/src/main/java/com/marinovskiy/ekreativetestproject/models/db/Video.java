package com.marinovskiy.ekreativetestproject.models.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.rightutils.rightutils.db.ColumnIgnore;
import com.rightutils.rightutils.db.TableName;

@TableName("videos")
public class Video implements Parcelable {

    private String id;

    private String playlistId;

    private String title;

    private String pictureUrl;

    private String description;

    private String duration;

//    public Video() {
//    }

    public Video(String id, String playlistId, String title, String pictureUrl,
                 String description, String duration) {
        this.id = id;
        this.playlistId = playlistId;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.duration = duration;
    }

    protected Video(Parcel in) {
        id = in.readString();
        playlistId = in.readString();
        title = in.readString();
        pictureUrl = in.readString();
        description = in.readString();
        duration = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(playlistId);
        dest.writeString(title);
        dest.writeString(pictureUrl);
        dest.writeString(description);
        dest.writeString(duration);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
}
