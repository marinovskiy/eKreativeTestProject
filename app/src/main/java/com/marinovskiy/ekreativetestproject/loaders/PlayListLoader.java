package com.marinovskiy.ekreativetestproject.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.marinovskiy.ekreativetestproject.api.youtube.YoutubeApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Map<String, String> playListParameters = new HashMap<>();
        playListParameters.put("part", "snippet");
        playListParameters.put("playlistId", mPlaylistId);
        playListParameters.put("maxResults", "10");
        NetworkYoutubeResponse response = null;
        try {
            response = YoutubeApiManager.getInstance().getPlaylist(playListParameters).execute().body();
            List<String> videoIdList = new ArrayList<>();
            String videoIds = "";
            for (int i = 0; i < response.getVideoList().size(); i++) {
                videoIds = videoIds + response.getVideoList().get(i).getSnippet().getResourceId().getVideoId() + ",";
            }
            Map<String, String> videosParameters = new HashMap<>();
            videosParameters.put("part", "snippet,contentDetails,statistics");
            videosParameters.put("id", videoIds);
            //playListParameters.put("maxResults", "10");
            response = YoutubeApiManager.getInstance().getVideos(videosParameters).execute().body();
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