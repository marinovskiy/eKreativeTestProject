package com.marinovskiy.ekreativetestproject.api.facebook;

import com.marinovskiy.ekreativetestproject.managers.PreferenceManager;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacebookApiManager {

    private static FacebookApiService sInstance;

    public static synchronized FacebookApiService getInstance() {
        if (sInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    HttpUrl httpUrl = chain.request().url()
                            .newBuilder()
                            .addQueryParameter(FacebookApiConstants.ACCESS_TOKEN,
                                    PreferenceManager.getAccessToken())
                            .addQueryParameter(FacebookApiConstants.FIELDS,
                                    FacebookApiConstants.FIELDS_PARAM)
                            .build();

                    Request request = chain.request()
                            .newBuilder()
                            .url(httpUrl)
                            .build();
                    return chain.proceed(request);
                }
            };

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(interceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FacebookApiConstants.BASE_URL + FacebookApiConstants.API_VERSION)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sInstance = retrofit.create(FacebookApiService.class);
        }
        return sInstance;
    }
}