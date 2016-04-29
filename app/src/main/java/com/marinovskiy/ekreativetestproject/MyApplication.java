package com.marinovskiy.ekreativetestproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.marinovskiy.ekreativetestproject.db.DbUtils;
import com.marinovskiy.ekreativetestproject.managers.PreferenceManager;

public class MyApplication extends Application {

    public static DbUtils sDbUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        sDbUtils = DbUtils.newInstance(this, "my_db.sqlite", 1);
    }
}
