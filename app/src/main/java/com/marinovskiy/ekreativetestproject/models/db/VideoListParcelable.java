package com.marinovskiy.ekreativetestproject.models.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class VideoListParcelable implements Parcelable {

    private List<VideoParcelable> videoParcelableList;

    public VideoListParcelable() {
    }

    public VideoListParcelable(List<VideoParcelable> videoParcelableList) {
        this.videoParcelableList = videoParcelableList;
    }

    protected VideoListParcelable(Parcel in) {
        videoParcelableList = in.createTypedArrayList(VideoParcelable.CREATOR);
    }

    public static final Creator<VideoListParcelable> CREATOR = new Creator<VideoListParcelable>() {
        @Override
        public VideoListParcelable createFromParcel(Parcel in) {
            return new VideoListParcelable(in);
        }

        @Override
        public VideoListParcelable[] newArray(int size) {
            return new VideoListParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(videoParcelableList);
    }

    public List<VideoParcelable> getVideoParcelableList() {
        return videoParcelableList;
    }

    public void setVideoParcelableList(List<VideoParcelable> videoParcelableList) {
        this.videoParcelableList = videoParcelableList;
    }
}
