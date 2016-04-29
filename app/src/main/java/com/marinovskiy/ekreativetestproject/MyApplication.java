package com.marinovskiy.ekreativetestproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.marinovskiy.ekreativetestproject.managers.PreferenceManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
