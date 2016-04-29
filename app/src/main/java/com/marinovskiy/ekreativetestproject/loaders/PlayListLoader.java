package com.marinovskiy.ekreativetestproject.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.marinovskiy.ekreativetestproject.api.youtube.YoutubeApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayListLoader extends AsyncTaskLoader<NetworkYoutubeResponse> {

    public static final String LOADER_KEY_PLAYLIST_ID = "loader_key_playlist_id";
    public static final String LOADER_KEY_NEXT_PAGE_TOKEN = "loader_key_next_page_token";

    private String mPlaylistId;
    private String mNextPageToken;

    public PlayListLoader(Context context, Bundle args) {
        super(context);
        Log.i("loaderlifecycle", "PlayListLoader()");
        mPlaylistId = args.getString(LOADER_KEY_PLAYLIST_ID);
        mNextPageToken = args.getString(LOADER_KEY_NEXT_PAGE_TOKEN);
    }

    @Override
    public NetworkYoutubeResponse loadInBackground() {
        Log.i("loaderlifecycle", "loadInBackground");
        Map<String, String> playListParameters = new HashMap<>();
        playListParameters.put("part", "snippet");
        playListParameters.put("playlistId", mPlaylistId);
        playListParameters.put("maxResults", "10");
        if (mNextPageToken != null) {
            playListParameters.put("pageToken", mNextPageToken);
            Log.i("loaderslogtags", "in loader: put into MAP" + mNextPageToken);
        }
        NetworkYoutubeResponse response = null;
        try {
            response = YoutubeApiManager.getInstance().getPlaylist(playListParameters).execute().body();
            /*mNextPageToken = response.getNextPageToken();
            Log.i("loaderslogtags", "in loader: get from response" + mNextPageToken);

            String videoIds = "";
            for (int i = 0; i < response.getVideoList().size(); i++) {
                videoIds = videoIds + response.getVideoList().get(i).getSnippet().getResourceId().getVideoId() + ",";
            }
            Map<String, String> videosParameters = new HashMap<>();
            videosParameters.put("part", "snippet,contentDetails,statistics");
            videosParameters.put("id", videoIds);
            response = YoutubeApiManager.getInstance().getVideos(videosParameters).execute().body();
            response.setNextPageToken(mNextPageToken);
            mNextPageToken = null;*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
        Log.i("loaderlifecycle", "forceLoad");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i("loaderlifecycle", "onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.i("loaderlifecycle", "onStopLoading");
    }

    @Override
    public void deliverResult(NetworkYoutubeResponse data) {
        super.deliverResult(data);
        Log.i("loaderlifecycle", "deliverResult");
    }
}