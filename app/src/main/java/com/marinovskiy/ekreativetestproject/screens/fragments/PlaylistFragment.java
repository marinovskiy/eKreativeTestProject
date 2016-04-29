package com.marinovskiy.ekreativetestproject.screens.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.loaders.PlayListLoader;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;
import com.marinovskiy.ekreativetestproject.ui.adapters.PlayListAdapter;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnItemClickListener;

import butterknife.Bind;

public class PlaylistFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<NetworkYoutubeResponse> {

    private static final String KEY_PLAYLIST_ID = "bundle_key_playlist_id";

    private static final int LOADER_PLAYLIST_ID = 1;

    @Bind(R.id.rv_playlist)
    RecyclerView mRvPlaylist;

    private String mPlaylistId;

    public static PlaylistFragment newInstance(String playlistId) {
        PlaylistFragment playlistFragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PLAYLIST_ID, playlistId);
        playlistFragment.setArguments(args);
        return playlistFragment;
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
        PlayListAdapter playListAdapter = new PlayListAdapter(data.getVideoList());
        mRvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvPlaylist.setAdapter(playListAdapter);
        playListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "Pos = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<NetworkYoutubeResponse> loader) {
    }
}
