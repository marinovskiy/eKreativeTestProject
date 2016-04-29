package com.marinovskiy.ekreativetestproject.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.loaders.PlayListLoader;
import com.marinovskiy.ekreativetestproject.models.NetworkVideo;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;
import com.marinovskiy.ekreativetestproject.screens.activities.PlayActivity;
import com.marinovskiy.ekreativetestproject.ui.adapters.PlayListAdapter;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnItemClickListener;

import java.util.List;

import butterknife.Bind;

public class PlayListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<NetworkYoutubeResponse> {

    private static final String KEY_PLAYLIST_ID = "bundle_key_playlist_id";

    private static final int LOADER_PLAYLIST_ID = 1;

    @Bind(R.id.rv_playlist)
    RecyclerView mRvPlaylist;

    private String mPlaylistId;

    private List<NetworkVideo> mVideoList;

    public static PlayListFragment newInstance(String playlistId) {
        PlayListFragment playListFragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PLAYLIST_ID, playlistId);
        playListFragment.setArguments(args);
        return playListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaylistId = getArguments().getString(KEY_PLAYLIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = new Bundle();
        args.putString(PlayListLoader.LOADER_KEY_PLAYLIST_ID, mPlaylistId);
        getLoaderManager().initLoader(LOADER_PLAYLIST_ID, args, this).forceLoad();
    }

    @Override
    public Loader<NetworkYoutubeResponse> onCreateLoader(int id, Bundle args) {
        return new PlayListLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<NetworkYoutubeResponse> loader, NetworkYoutubeResponse data) {
        mVideoList = data.getVideoList();
        PlayListAdapter playListAdapter = new PlayListAdapter(mVideoList);
        mRvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvPlaylist.setAdapter(playListAdapter);
        playListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent playVideoIntent = new Intent(getActivity(), PlayActivity.class);
                playVideoIntent.putExtra(PlayActivity.INTENT_KEY_VIDEO_ID, mVideoList.get(position).getId());
                startActivity(playVideoIntent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<NetworkYoutubeResponse> loader) {
    }
}
