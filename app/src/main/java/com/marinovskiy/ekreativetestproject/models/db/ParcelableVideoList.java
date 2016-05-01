package com.marinovskiy.ekreativetestproject.models.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ParcelableVideoList implements Parcelable {

    private List<Video> videoList;

    public ParcelableVideoList() {
    }

    public ParcelableVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    protected ParcelableVideoList(Parcel in) {
        videoList = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<ParcelableVideoList> CREATOR = new Creator<ParcelableVideoList>() {
        @Override
        public ParcelableVideoList createFromParcel(Parcel in) {
            return new ParcelableVideoList(in);
        }

        @Override
        public ParcelableVideoList[] newArray(int size) {
            return new ParcelableVideoList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(videoList);
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
