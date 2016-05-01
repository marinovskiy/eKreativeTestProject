package com.marinovskiy.ekreativetestproject.applications;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.marinovskiy.ekreativetestproject.db.DbUtils;
import com.marinovskiy.ekreativetestproject.managers.PreferenceManager;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

    public static DbUtils sDbUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        PreferenceManager.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        sDbUtils = DbUtils.newInstance(this, "ekreative_test_db.sqlite", 1);
    }
}