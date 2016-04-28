package com.marinovskiy.ekreativetestproject.api.youtube;

import com.marinovskiy.ekreativetestproject.models.NetworkUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/{user-id}")
    Call<NetworkUser> getUserProfile(@Path("user-id") String userId);

}
