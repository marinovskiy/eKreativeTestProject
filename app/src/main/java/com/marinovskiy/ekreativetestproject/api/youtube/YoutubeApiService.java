package com.marinovskiy.ekreativetestproject.api.youtube;

import com.marinovskiy.ekreativetestproject.models.NetworkUser;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface YoutubeApiService {

    @GET(YoutubeApiConstants.PLAYLIST_ITEMS)
    Call<NetworkYoutubeResponse> getPlaylist(@QueryMap Map<String, String> parameters);

    @GET(YoutubeApiConstants.VIDEOS)
    Call<NetworkYoutubeResponse> getVideos(@QueryMap Map<String, String> parameters);

}
