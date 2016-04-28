package com.marinovskiy.ekreativetestproject.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.api.facebook.ApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkUser;
import com.marinovskiy.ekreativetestproject.screens.utils.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        fetchUserProfile();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_first_playlist:
                break;
            case R.id.action_nav_second_playlist:
                break;
            case R.id.action_nav_third_playlist:
                break;
            case R.id.action_nav_logout:
                logOut();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchUserProfile() {
        ApiManager.getInstance().getUserProfile(Prefs.getUserId()).enqueue(new Callback<NetworkUser>() {
            @Override
            public void onResponse(Call<NetworkUser> call, Response<NetworkUser> response) {
                if (response.isSuccessful()) {
                    updateNavHeader(response.body());
                }
            }

            @Override
            public void onFailure(Call<NetworkUser> call, Throwable t) {
            }
        });
    }

    private void updateNavHeader(NetworkUser user) {
        View navigationHeader = mNavigationView.getHeaderView(0);
        ImageView coverPhoto = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_cover);
        ImageView userAvatar = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_avatar);
        TextView userName = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_name);
        TextView userEmail = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_email);

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getCover().getUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(coverPhoto);
        Glide.with(this)
                .load(user.getPicture().getData().getUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(userAvatar);
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        Prefs.setAccessToken(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
