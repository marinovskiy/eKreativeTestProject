package com.marinovskiy.ekreativetestproject.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.marinovskiy.ekreativetestproject.api.youtube.YoutubeApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayListLoader extends AsyncTaskLoader<NetworkYoutubeResponse> {

    public static final String LOADER_KEY_PLAYLIST_ID = "loader_key_playlist_id";

    private String mPlaylistId;

    public PlayListLoader(Context context, Bundle args) {
        super(context);
        mPlaylistId = args.getString(LOADER_KEY_PLAYLIST_ID);
    }

    @Override
    public NetworkYoutubeResponse loadInBackground() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("playlistId", mPlaylistId);
        parameters.put("maxResults", "10");
        NetworkYoutubeResponse response = null;
        try {
            response = YoutubeApiManager.getInstance()
                    .getPlaylist(parameters).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    public void deliverResult(NetworkYoutubeResponse data) {
        super.deliverResult(data);
    }
}