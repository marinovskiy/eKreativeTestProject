package com.marinovskiy.ekreativetestproject.managers;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class AuthManager {

    public static void login(LoginResult loginResult) {
        PreferenceManager.setAccessToken(loginResult.getAccessToken().getToken());
        PreferenceManager.setUserId(loginResult.getAccessToken().getUserId());
    }

    public static boolean isUserLoggedIn() {
        return PreferenceManager.getAccessToken() != null;
    }

    public static void logOut() {
        LoginManager.getInstance().logOut();
        PreferenceManager.setAccessToken(null);
        PreferenceManager.setUserId(null);
    }

}