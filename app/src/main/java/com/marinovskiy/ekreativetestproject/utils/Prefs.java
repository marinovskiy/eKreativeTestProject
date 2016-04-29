package com.marinovskiy.ekreativetestproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String KEY_ACCESS_TOKEN = "prefs_key_access_token";
    private static final String KEY_USER_ID = "prefs_key_user_id";
    private static final String KEY_NEXT_PAGE_TOKEN = "prefs_key_next_page_token";

    private static SharedPreferences sPrefs;

    private Prefs() {
    }

    public static void init(Context context) {
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static void setAccessToken(String value) {
        updateValue(KEY_ACCESS_TOKEN, value);
    }

    public static String getAccessToken() {
        return sPrefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void setUserId(String value) {
        updateValue(KEY_USER_ID, value);
    }

    public static String getUserId() {
        return sPrefs.getString(KEY_USER_ID, null);
    }

    public static void setNextPageToken(String value) {
        updateValue(KEY_NEXT_PAGE_TOKEN, value);
    }

    public static String getNextPageToken() {
        return sPrefs.getString(KEY_NEXT_PAGE_TOKEN, null);
    }

    private static void updateValue(String key, String value) {
        sPrefs.edit()
                .putString(key, value)
                .apply();
    }

}
