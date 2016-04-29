package com.marinovskiy.ekreativetestproject.screens.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.api.youtube.YoutubeApiManager;
import com.marinovskiy.ekreativetestproject.loaders.PlayListLoader;
import com.marinovskiy.ekreativetestproject.models.NetworkVideo;
import com.marinovskiy.ekreativetestproject.models.NetworkYoutubeResponse;
import com.marinovskiy.ekreativetestproject.screens.activities.PlayVideoActivity;
import com.marinovskiy.ekreativetestproject.ui.adapters.PlayListAdapter;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnItemClickListener;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnLoadMoreListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class PlayListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<NetworkYoutubeResponse> {

    private static final String KEY_PLAYLIST_ID = "bundle_key_playlist_id";

    private static final int LOADER_PLAYLIST_ID = 1;

    @Bind(R.id.rv_playlist)
    RecyclerView mRvPlaylist;

    private static String mPlaylistId;

    private List<NetworkVideo> mVideoList;

    private static String mNextPageToken;
    private PlayListAdapter playListAdapter;

    private boolean mIsLoadedMore = false;
    private int mTotalResults;

    private Bundle mArgs;

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

        mRvPlaylist.setHasFixedSize(true);
        mRvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));

        mArgs = new Bundle();
        mArgs.putString(PlayListLoader.LOADER_KEY_PLAYLIST_ID, mPlaylistId);
        getLoaderManager().initLoader(LOADER_PLAYLIST_ID, mArgs, this);
    }

    @Override
    public Loader<NetworkYoutubeResponse> onCreateLoader(int id, Bundle args) {
        Log.i("loaderlifecycle", "onCreateLoader()");
        return new PlayListLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<NetworkYoutubeResponse> loader, NetworkYoutubeResponse data) {
        Log.i("loaderlifecycle", "onLoadFinished()");
        if (!mIsLoadedMore) {
            mVideoList = data.getVideoList();
            mNextPageToken = data.getNextPageToken();
            mTotalResults = data.getPageInfo().getTotalResults();
            Log.i("loaderslogtags", "in fragment: get from data response1" + mNextPageToken);

            playListAdapter = new PlayListAdapter(mVideoList, mRvPlaylist);
            mRvPlaylist.setAdapter(playListAdapter);

            playListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent playVideoIntent = new Intent(getActivity(), PlayVideoActivity.class);
                    playVideoIntent.putExtra(PlayVideoActivity.INTENT_KEY_VIDEO_ID, mVideoList.get(position).getId());
                    startActivity(playVideoIntent);
                }
            });

            playListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (mVideoList.size() < mTotalResults) {
                        mIsLoadedMore = true;

                        mVideoList.add(null);
                        playListAdapter.notifyItemInserted(mVideoList.size() - 1);

                        loadNextVideos();
                    }
                }
            });
        } else {
            mNextPageToken = data.getNextPageToken();
            Log.i("loaderslogtags", "in fragment: get from data response2" + mNextPageToken);
            mVideoList.remove(mVideoList.size() - 1);
            playListAdapter.notifyItemRemoved(mVideoList.size());
            playListAdapter.setLoaded();
            for (int i = 0; i < data.getVideoList().size(); i++) {
                mVideoList.add(data.getVideoList().get(i));
                playListAdapter.notifyItemInserted(mVideoList.size());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<NetworkYoutubeResponse> loader) {
        Log.i("loaderlifecycle", "onLoaderReset()");
    }

    private void loadNextVideos() {
        Log.i("loaderslogtags", "in fragment: in loadNextVideos" + mNextPageToken);
        mArgs.putString(PlayListLoader.LOADER_KEY_NEXT_PAGE_TOKEN, mNextPageToken);
        getLoaderManager().restartLoader(LOADER_PLAYLIST_ID, mArgs, this);
    }

    static class PlayListLoader1 extends AsyncTaskLoader<NetworkYoutubeResponse> {

        public PlayListLoader1(Context context, Bundle args) {
            super(context);
            Log.i("loaderlifecycle", "PlayListLoader()");
        }

        @Override
        public NetworkYoutubeResponse loadInBackground() {
            Log.i("loaderlifecycle", "loadInBackground");
            Map<String, String> playListParameters = new HashMap<>();
            playListParameters.put("part", "snippet");
            playListParameters.put("playlistId", mPlaylistId);
            playListParameters.put("maxResults", "10");
            if (mNextPageToken != null) {
                playListParameters.put("pageToken", mNextPageToken);
                Log.i("loaderslogtags", "in loader: put into MAP" + mNextPageToken);
            }
            NetworkYoutubeResponse response = null;
            try {
                response = YoutubeApiManager.getInstance().getPlaylist(playListParameters).execute().body();
                mNextPageToken = response.getNextPageToken();
                Log.i("loaderslogtags", "in loader: get from response" + mNextPageToken);

                /*String videoIds = "";
                for (int i = 0; i < response.getVideoList().size(); i++) {
                    videoIds = videoIds + response.getVideoList().get(i).getSnippet().getResourceId().getVideoId() + ",";
                }
                Map<String, String> videosParameters = new HashMap<>();
                videosParameters.put("part", "snippet,contentDetails,statistics");
                videosParameters.put("id", videoIds);
                response = YoutubeApiManager.getInstance().getVideos(videosParameters).execute().body();*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        public void forceLoad() {
            super.forceLoad();
            Log.i("loaderlifecycle", "forceLoad");
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.i("loaderlifecycle", "onStartLoading");
            forceLoad();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Log.i("loaderlifecycle", "onStopLoading");
        }

        @Override
        public void deliverResult(NetworkYoutubeResponse data) {
            super.deliverResult(data);
            Log.i("loaderlifecycle", "deliverResult");
        }
    }
}