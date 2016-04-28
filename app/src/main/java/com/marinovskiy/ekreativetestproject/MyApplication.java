package com.marinovskiy.ekreativetestproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.marinovskiy.ekreativetestproject.screens.utils.Prefs;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
