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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.api.facebook.FacebookApiManager;
import com.marinovskiy.ekreativetestproject.models.NetworkUser;
import com.marinovskiy.ekreativetestproject.screens.fragments.PlayListFragment;
import com.marinovskiy.ekreativetestproject.utils.Prefs;

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

    @Bind(R.id.frame_container_main)
    FrameLayout mFrameContainer;

    private PlayListFragment mFirstPlaylist;
    private PlayListFragment mSecondPlaylist;
    private PlayListFragment mThirdPlaylist;

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

        mFirstPlaylist = PlayListFragment.newInstance("PL0vcQp16X6WjG9P9Rzu3sfGMS4404zPDi");
        mSecondPlaylist = PlayListFragment.newInstance("PL16B22E52465A93A4");
        mThirdPlaylist = PlayListFragment.newInstance("PLWz5rJ2EKKc9ofd2f-_-xmUi07wIGZa1c");

        mNavigationView.setCheckedItem(R.id.action_nav_first_playlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container_main,
                            mFirstPlaylist,
                            PlayListFragment.class.getSimpleName() + "1")
                    .commit();
        }

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
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_container_main,
                                mFirstPlaylist,
                                PlayListFragment.class.getSimpleName() + "1")
                        .addToBackStack("1")
                        .commit();
                break;
            case R.id.action_nav_second_playlist:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_container_main,
                                mSecondPlaylist,
                                PlayListFragment.class.getSimpleName() + "2")
                        .addToBackStack("2")
                        .commit();
                break;
            case R.id.action_nav_third_playlist:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_container_main,
                                mThirdPlaylist,
                                PlayListFragment.class.getSimpleName() + "3")
                        .addToBackStack("3")
                        .commit();
                break;
            case R.id.action_nav_logout:
                logOut();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchUserProfile() {
        FacebookApiManager.getInstance().getUserProfile(Prefs.getUserId()).enqueue(new Callback<NetworkUser>() {
            @Override
            public void onResponse(Call<NetworkUser> call, Response<NetworkUser> response) {
                if (response.isSuccessful()) {
                    updateUi(response.body());
                }
            }

            @Override
            public void onFailure(Call<NetworkUser> call, Throwable t) {
            }
        });
    }

    private void updateUi(NetworkUser user) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(user.getName());
        }

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
