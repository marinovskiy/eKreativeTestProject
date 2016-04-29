package com.marinovskiy.ekreativetestproject.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.utils.Prefs;

import butterknife.Bind;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.btn_login)
    LoginButton mBtnLogin;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Prefs.getAccessToken() != null) {
            startMainActivity();
        }

        setContentView(R.layout.activity_login);
        mBtnLogin.setReadPermissions("public_profile", "email");

        mCallbackManager = CallbackManager.Factory.create();
        mBtnLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Prefs.setAccessToken(loginResult.getAccessToken().getToken());
                Prefs.setUserId(loginResult.getAccessToken().getUserId());
                startMainActivity();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Authorization error: " + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}