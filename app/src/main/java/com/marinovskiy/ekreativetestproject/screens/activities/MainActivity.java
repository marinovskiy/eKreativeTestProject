package com.marinovskiy.ekreativetestproject.screens.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
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
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.applications.MyApplication;
import com.marinovskiy.ekreativetestproject.loaders.UserLoader;
import com.marinovskiy.ekreativetestproject.managers.AuthManager;
import com.marinovskiy.ekreativetestproject.managers.ModelConverter;
import com.marinovskiy.ekreativetestproject.managers.PreferenceManager;
import com.marinovskiy.ekreativetestproject.managers.Utils;
import com.marinovskiy.ekreativetestproject.models.db.User;
import com.marinovskiy.ekreativetestproject.models.network.NetworkUser;
import com.marinovskiy.ekreativetestproject.screens.fragments.PlayListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<NetworkUser> {

    private static final int LOADER_USER_ID = 0;

    @Bind(R.id.toolbar_main)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_shadow_main)
    View mToolbarShadow;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private int mDrawerPosition = 0;
    private PlayListFragment mCurrentFragment;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbarShadow.setVisibility(View.GONE);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.action_nav_first_playlist);

        mUserId = PreferenceManager.getUserId();

        if (Utils.hasInternet(getApplicationContext()) && mUserId != null) {
            Bundle args = new Bundle();
            args.putString(UserLoader.LOADER_KEY_USER_ID, mUserId);
            getLoaderManager().initLoader(LOADER_USER_ID, args, this);
        } else {
            updateUi(MyApplication.sDbUtils.getUser(PreferenceManager.getUserId()));
        }

        if (savedInstanceState == null) {
            mDrawerPosition = 1;
            mCurrentFragment = PlayListFragment.newInstance(getString(R.string.playlist_id_first));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container_main,
                            mCurrentFragment)
                    .commit();
        }
    }

    @Override
    public Loader<NetworkUser> onCreateLoader(int id, Bundle args) {
        return new UserLoader(getApplicationContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<NetworkUser> loader, NetworkUser data) {
        User user = ModelConverter.convertToUser(data);
        MyApplication.sDbUtils.saveUser(user);
        updateUi(user);
    }

    @Override
    public void onLoaderReset(Loader<NetworkUser> loader) {
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
                if (mDrawerPosition != 1) {
                    mCurrentFragment = PlayListFragment
                            .newInstance(getString(R.string.playlist_id_first));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.frame_container_main,
                                    mCurrentFragment)
                            .commit();
                    mDrawerPosition = 1;
                }
                break;
            case R.id.action_nav_second_playlist:
                if (mDrawerPosition != 2) {
                    mCurrentFragment = PlayListFragment
                            .newInstance(getString(R.string.playlist_id_second));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.frame_container_main,
                                    mCurrentFragment)
                            .commit();
                    mDrawerPosition = 2;
                }
                break;
            case R.id.action_nav_third_playlist:
                if (mDrawerPosition != 3) {
                    mCurrentFragment = PlayListFragment
                            .newInstance(getString(R.string.playlist_id_third));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.frame_container_main,
                                    mCurrentFragment)
                            .commit();
                    mDrawerPosition = 3;
                }
                break;
            case R.id.action_nav_logout:
                if (mUserId != null) {
                    logOut();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUi(User user) {

        View navigationHeader = mNavigationView.getHeaderView(0);
        ImageView coverPhoto = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_cover);
        ImageView userAvatar = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_avatar);
        TextView userName = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_name);
        TextView userEmail = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_email);

        if (user != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(user.getName());
            }

            userName.setText(user.getName());
            if (!user.getEmail().equals("")) {
                userEmail.setText(user.getEmail());
            } else {
                userEmail.setVisibility(View.GONE);
            }

            if (!user.getCoverUrl().equals("")) {
                Glide.with(this)
                        .load(user.getCoverUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(coverPhoto);
            }
            if (!user.getAvatarUrl().equals("")) {
                Glide.with(this)
                        .load(user.getAvatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(userAvatar);
            }
        } else {
            userEmail.setVisibility(View.GONE);
            MenuItem menuItemLogout = mNavigationView.getMenu().findItem(R.id.action_nav_logout);
            menuItemLogout.setTitle(R.string.action_login);
            menuItemLogout.setIcon(R.drawable.ic_login);
        }
    }

    private void logOut() {
        AuthManager.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}