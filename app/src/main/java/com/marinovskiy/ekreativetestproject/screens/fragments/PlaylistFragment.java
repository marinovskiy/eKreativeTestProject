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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.adapters.PlayListAdapter;
import com.marinovskiy.ekreativetestproject.db.DbUtils;
import com.marinovskiy.ekreativetestproject.interfaces.OnItemClickListener;
import com.marinovskiy.ekreativetestproject.loaders.PlayListLoader;
import com.marinovskiy.ekreativetestproject.managers.ModelConverter;
import com.marinovskiy.ekreativetestproject.managers.Utils;
import com.marinovskiy.ekreativetestproject.models.db.ParcelableVideoList;
import com.marinovskiy.ekreativetestproject.models.db.Video;
import com.marinovskiy.ekreativetestproject.models.network.NetworkYoutubeResponse;
import com.marinovskiy.ekreativetestproject.screens.activities.PlayVideoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class PlayListFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<NetworkYoutubeResponse> {

    private static final String BUNDLE_KEY_PLAYLIST_ID = "bundle_key_playlist_id";

    private static final String BUNDLE_STATE_VIDEO_LIST = "bundle_video_list";
    private static final String BUNDLE_STATE_NEXT_PAGE_TOKEN = "bundle_next_page_token";
    private static final String BUNDLE_STATE_TOTAL_RESULTS = "bundle_total_results";
    private static final String BUNDLE_STATE_RV_POSITION = "bundle_next_rv_position";

    private static final int LOADER_PLAYLIST_ID = 1;

    @Bind(R.id.rv_playlist)
    RecyclerView mRvPlaylist;

    @Bind(R.id.pb_fragment_playlist)
    ProgressBar mProgressBar;

    @Bind(R.id.empty_view)
    LinearLayout mEmptyView;

    private LinearLayoutManager mLinearLayoutManager;
    private PlayListAdapter playListAdapter;

    private String mPlaylistId;
    private String mNextPageToken;
    private List<Video> mVideoList = new ArrayList<>();
    private int mTotalResults;

    private Bundle mArgs;

    private int mVisibleThreshold = 4;
    private int mLastVisibleItem;
    private int mLoadedItems;
    private boolean mLoading;

    private boolean mIsNeedToLoad = true;

    public static PlayListFragment newInstance(String playlistId) {
        PlayListFragment playListFragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_PLAYLIST_ID, playlistId);
        playListFragment.setArguments(args);
        return playListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaylistId = getArguments().getString(BUNDLE_KEY_PLAYLIST_ID);
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

        if (savedInstanceState != null) {
            Log.i("logtags", "restore");
            ParcelableVideoList parcelableVideoList = savedInstanceState
                    .getParcelable(BUNDLE_STATE_VIDEO_LIST);
            if (parcelableVideoList != null) {
                updateUi(parcelableVideoList.getVideoList());
            }
            mNextPageToken = savedInstanceState.getString(BUNDLE_STATE_NEXT_PAGE_TOKEN);
            mTotalResults = savedInstanceState.getInt(BUNDLE_STATE_TOTAL_RESULTS);
            mRvPlaylist.scrollToPosition(savedInstanceState.getInt(BUNDLE_STATE_RV_POSITION));
            mIsNeedToLoad = false;
        }

        mArgs = new Bundle();
        mArgs.putString(PlayListLoader.LOADER_KEY_PLAYLIST_ID, mPlaylistId);
        if (mIsNeedToLoad) {
            setProgressBarVisibility(true);
            if (Utils.hasInternet(getContext())) {
                getLoaderManager().initLoader(LOADER_PLAYLIST_ID, mArgs, this);
            } else {
                updateUi(DbUtils.getVideos(mPlaylistId));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("logtags", "save");
        ParcelableVideoList parcelableVideoList = new ParcelableVideoList(mVideoList);
        outState.putParcelable(BUNDLE_STATE_VIDEO_LIST, parcelableVideoList);
        outState.putString(BUNDLE_STATE_NEXT_PAGE_TOKEN, mNextPageToken);
        outState.putInt(BUNDLE_STATE_TOTAL_RESULTS, mTotalResults);
        outState.putInt(BUNDLE_STATE_RV_POSITION, mLastVisibleItem);
    }

    @Override
    public Loader<NetworkYoutubeResponse> onCreateLoader(int id, Bundle args) {
        return new PlayListLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<NetworkYoutubeResponse> loader, NetworkYoutubeResponse data) {
        mTotalResults = data.getPageInfo().getTotalResults();
        mNextPageToken = data.getNextPageToken();
        List<Video> videoList = ModelConverter.convertToVideos(data.getVideoList(), mPlaylistId);
        DbUtils.saveVideos(videoList);
        updateUi(videoList);
    }

    @Override
    public void onLoaderReset(Loader<NetworkYoutubeResponse> loader) {
    }

    private void updateUi(List<Video> videoList) {
        setProgressBarVisibility(false);
        if (videoList == null || videoList.size() == 0) {
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
                        if (Utils.hasInternet(getContext())) {
                            Intent playVideoIntent = new Intent(getActivity(),
                                    PlayVideoActivity.class);
                            playVideoIntent.putExtra(PlayVideoActivity.INTENT_KEY_VIDEO_ID,
                                    mVideoList.get(position).getId());
                            startActivity(playVideoIntent);
                        } else {
                            Toast.makeText(getContext(), R.string.toast_play_video_no_internet,
                                    Toast.LENGTH_SHORT).show();
                        }
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

    private void setProgressBarVisibility(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}