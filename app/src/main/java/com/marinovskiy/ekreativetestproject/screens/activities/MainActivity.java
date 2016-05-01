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
import com.marinovskiy.ekreativetestproject.db.DbUtils;
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

    private static final int LOADER_USER_ID = 0; // TODO STRINGS

    @Bind(R.id.toolbar_main)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_shadow_main)
    View mToolbarShadow;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private int mDrawerPosition = 1;
    private PlayListFragment mCurrentFragment;

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

        if (Utils.hasInternet(getApplicationContext())) {
            Bundle args = new Bundle();
            args.putString(UserLoader.LOADER_KEY_USER_ID, PreferenceManager.getUserId());
            getLoaderManager().initLoader(LOADER_USER_ID, args, this);
        } else {
            updateUi(DbUtils.getUser(PreferenceManager.getUserId()));
        }

        if (savedInstanceState == null) {
            mCurrentFragment = PlayListFragment.newInstance("PL0vcQp16X6WjG9P9Rzu3sfGMS4404zPDi");
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
        DbUtils.saveUser(user);
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
                    mCurrentFragment = PlayListFragment.newInstance("PL0vcQp16X6WjG9P9Rzu3sfGMS4404zPDi");
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
                    mCurrentFragment = PlayListFragment.newInstance("PL16B22E52465A93A4");
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
                    mCurrentFragment = PlayListFragment.newInstance("PLWz5rJ2EKKc9ofd2f-_-xmUi07wIGZa1c");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.frame_container_main,
                                    mCurrentFragment)
                            .commit();
                    mDrawerPosition = 3;
                }
                break;
            case R.id.action_nav_logout:
                logOut();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUi(User user) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(user.getName());
        }

        View navigationHeader = mNavigationView.getHeaderView(0);
        ImageView coverPhoto = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_cover);
        ImageView userAvatar = ButterKnife.findById(navigationHeader, R.id.nav_header_iv_avatar);
        TextView userName = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_name);
        TextView userEmail = ButterKnife.findById(navigationHeader, R.id.nav_header_tv_email);

        userName.setText(user.getName());
        if (!user.getEmail().equals("")) {
            userEmail.setText(user.getEmail());
        } else {
            userEmail.setVisibility(View.GONE);
        }

        if (!user.getCoverUrl().equals("")) {
            Glide.with(this)
                    .load(user.getCoverUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(coverPhoto);
        }
        if (!user.getAvatarUrl().equals("")) {
            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(userAvatar);
        }
    }

    private void logOut() {
        AuthManager.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}