package com.marinovskiy.ekreativetestproject.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.adapters.PlayListAdapter;
import com.marinovskiy.ekreativetestproject.interfaces.OnItemClickListener;
import com.marinovskiy.ekreativetestproject.loaders.PlayListLoader;
import com.marinovskiy.ekreativetestproject.managers.ModelConverter;
import com.marinovskiy.ekreativetestproject.managers.Utils;
import com.marinovskiy.ekreativetestproject.models.db.ParcelableVideoList;
import com.marinovskiy.ekreativetestproject.models.db.Video;
import com.marinovskiy.ekreativetestproject.models.network.NetworkYoutubeResponse;
import com.marinovskiy.ekreativetestproject.screens.activities.PlayVideoActivity;

import java.util.List;

import butterknife.Bind;

public class PlayListFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<NetworkYoutubeResponse> {

    private static final String KEY_PLAYLIST_ID = "bundle_key_playlist_id";

    private static final int LOADER_PLAYLIST_ID = 1;

    @Bind(R.id.rv_playlist)
    RecyclerView mRvPlaylist;

    @Bind(R.id.empty_view)
    LinearLayout mEmptyView;

    private LinearLayoutManager mLinearLayoutManager;
    private PlayListAdapter playListAdapter;

    private String mPlaylistId;
    private String mNextPageToken;
    private List<Video> mVideoList;
    private int mTotalResults;

    private Bundle mArgs;

    private int mVisibleThreshold = 4;
    private int mLastVisibleItem;
    private int mLoadedItems;
    private boolean mLoading;

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

        setRetainInstance(true);

        mRvPlaylist.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRvPlaylist.setLayoutManager(mLinearLayoutManager);

        if (Utils.hasInternet(getContext())) {
            mRvPlaylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    mLoadedItems = mLinearLayoutManager.getItemCount();
                    mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                    if (!mLoading && mLoadedItems <= (mLastVisibleItem + mVisibleThreshold)) {
                        if (mVideoList.size() < mTotalResults) {
                            mVideoList.add(null);
                            playListAdapter.notifyItemInserted(mVideoList.size() - 1);
                            loadNextVideos();
                        }
                        mLoading = true;
                    }
                }
            });
        }

        mArgs = new Bundle();
        mArgs.putString(PlayListLoader.LOADER_KEY_PLAYLIST_ID, mPlaylistId);

        if (Utils.hasInternet(getContext())) {
            getLoaderManager().initLoader(LOADER_PLAYLIST_ID, mArgs, this);
        } else {
            //updateUi(DbUtils.getVideosBy(mPlaylistId));
        }

        /*if (savedInstanceState != null) {
            Toast.makeText(getContext(), "playlist id = " + mPlaylistId, Toast.LENGTH_SHORT).show();
            /*ParcelableVideoList parcelableVideoList = savedInstanceState.getParcelable("bundle_video_list");
            if (parcelableVideoList != null) {
                mVideoList = parcelableVideoList.getVideoList(); try to clear mVideoList
            }*/
            /*mNextPageToken = savedInstanceState.getString("bundle_next_page_token");
            mTotalResults = savedInstanceState.getInt("bundle_next_total_results");
            int rvPos = savedInstanceState.getInt("bundle_next_rv_position");
            mRvPlaylist.scrollToPosition(rvPos);
            Log.i("resotorelogs", "onActivityCreated: " + mVideoList);
            Log.i("resotorelogs", "onActivityCreated: " + mNextPageToken);
            Log.i("resotorelogs", "onActivityCreated: " + mTotalResults);
            Log.i("resotorelogs", "onActivityCreated: " + rvPos);
        }*/
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ParcelableVideoList parcelableVideoList = new ParcelableVideoList(mVideoList);
        outState.putParcelable("bundle_video_list", parcelableVideoList);
        outState.putString("bundle_next_page_token", mNextPageToken);
        outState.putInt("bundle_next_total_results", mTotalResults);
        outState.putInt("bundle_next_rv_position", mLastVisibleItem);
        Log.i("resotorelogs", "onSaveInstanceState: " + parcelableVideoList.getVideoList());
        Log.i("resotorelogs", "onSaveInstanceState: " + mNextPageToken);
        Log.i("resotorelogs", "onSaveInstanceState: " + mTotalResults);
        Log.i("resotorelogs", "onSaveInstanceState: " + mLinearLayoutManager.findFirstVisibleItemPosition());
    }*/

    @Override
    public Loader<NetworkYoutubeResponse> onCreateLoader(int id, Bundle args) {
        return new PlayListLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<NetworkYoutubeResponse> loader, NetworkYoutubeResponse data) {
        mTotalResults = data.getPageInfo().getTotalResults();
        mNextPageToken = data.getNextPageToken();
        List<Video> videoList = ModelConverter.convertToVideos(data.getVideoList(), mPlaylistId);
//        DbUtils.saveVideos(videoList);
        updateUi(videoList);
    }

    @Override
    public void onLoaderReset(Loader<NetworkYoutubeResponse> loader) {
    }

    private void updateUi(List<Video> videoList) {
        if (videoList.size() == 0) { // TODO check when end DB
            mRvPlaylist.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            if (mRvPlaylist.getAdapter() == null) {
                mVideoList = videoList;

                playListAdapter = new PlayListAdapter(mVideoList);
                mRvPlaylist.setAdapter(playListAdapter);

                playListAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent playVideoIntent = new Intent(getActivity(), PlayVideoActivity.class);
                        playVideoIntent.putExtra(PlayVideoActivity.INTENT_KEY_VIDEO_ID,
                                mVideoList.get(position).getId());
                        startActivity(playVideoIntent);
                    }
                });
            } else {
                mVideoList.remove(mVideoList.size() - 1);
                playListAdapter.notifyItemRemoved(mVideoList.size());
                mLoading = false;

                for (int i = 0; i < videoList.size(); i++) {
                    mVideoList.add(videoList.get(i));
                    playListAdapter.notifyItemInserted(mVideoList.size());
                }
            }
        }
    }

    private void loadNextVideos() {
        mArgs.putString(PlayListLoader.LOADER_KEY_PLAYLIST_ID, mPlaylistId);
        mArgs.putString(PlayListLoader.LOADER_KEY_NEXT_PAGE_TOKEN, mNextPageToken);
        getLoaderManager().restartLoader(LOADER_PLAYLIST_ID, mArgs, this);
    }
}