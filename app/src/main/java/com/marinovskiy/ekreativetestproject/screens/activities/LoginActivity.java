package com.marinovskiy.ekreativetestproject.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.api.facebook.FacebookApiConstants;
import com.marinovskiy.ekreativetestproject.managers.AuthManager;
import com.marinovskiy.ekreativetestproject.managers.Utils;

import java.util.Arrays;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {

    @Bind(R.id.fab_skip_login)
    FloatingActionButton mFab;

    @Bind(R.id.btn_login)
    LinearLayout mLlLogin;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AuthManager.isUserLoggedIn()) {
            startMainActivity();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mCallbackManager = CallbackManager.Factory.create();
    }

    @OnClick({R.id.fab_skip_login, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_skip_login:
                startMainActivity();
                break;
            case R.id.btn_login:
                if (Utils.hasInternet(getApplicationContext())) {
                    mFab.setVisibility(View.GONE);
                    mLlLogin.setVisibility(View.GONE);
                    LoginManager.getInstance().logInWithReadPermissions(this,
                            Arrays.asList(FacebookApiConstants.READ_PERMISSIONS_PROFILE,
                                    FacebookApiConstants.READ_PERMISSIONS_EMAIL));
                    LoginManager.getInstance().registerCallback(mCallbackManager, this);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.toast_try_login_with_no_internet,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        AuthManager.login(loginResult);
        startMainActivity();
    }

    @Override
    public void onCancel() {
        mFab.setVisibility(View.VISIBLE);
        mLlLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(FacebookException error) {
        mFab.setVisibility(View.VISIBLE);
        mLlLogin.setVisibility(View.VISIBLE);
        Toast.makeText(LoginActivity.this, String.format(Locale.getDefault(),
                getString(R.string.toast_auth_error), error.toString()),
                Toast.LENGTH_SHORT).show();

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