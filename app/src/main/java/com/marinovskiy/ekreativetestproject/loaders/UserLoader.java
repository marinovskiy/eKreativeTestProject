package com.marinovskiy.ekreativetestproject.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.marinovskiy.ekreativetestproject.api.facebook.FacebookApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkUser;

import java.io.IOException;

public class UserLoader extends AsyncTaskLoader<NetworkUser> {

    public static final String LOADER_KEY_USER_ID = "loader_key_user_id";

    private String mUserId;

    public UserLoader(Context context, Bundle args) {
        super(context);
        mUserId = args.getString(LOADER_KEY_USER_ID);
    }

    @Override
    public NetworkUser loadInBackground() {
        NetworkUser networkUser = null;
        try {
            networkUser = FacebookApiManager.getInstance().getUserProfile(mUserId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return networkUser;
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
    public void deliverResult(NetworkUser data) {
        super.deliverResult(data);
    }

}
