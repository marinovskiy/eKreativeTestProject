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

    private static final int LOADER_USER_ID = 0;

    @Bind(R.id.toolbar_main)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_shadow_main)
    View mToolbarShadow;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private PlayListFragment mFirstPlaylist;
    private PlayListFragment mSecondPlaylist;
    private PlayListFragment mThirdPlaylist;

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

        mFirstPlaylist = PlayListFragment.newInstance("PL0vcQp16X6WjG9P9Rzu3sfGMS4404zPDi");
        mSecondPlaylist = PlayListFragment.newInstance("PL16B22E52465A93A4");
        mThirdPlaylist = PlayListFragment.newInstance("PLWz5rJ2EKKc9ofd2f-_-xmUi07wIGZa1c");

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container_main,
                            mFirstPlaylist,
                            PlayListFragment.class.getSimpleName() + "1")
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
        userEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getCoverUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(coverPhoto);
        Glide.with(this)
                .load(user.getAvatarUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(userAvatar);
    }

    private void logOut() {
        AuthManager.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
