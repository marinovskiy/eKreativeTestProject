package com.marinovskiy.ekreativetestproject.ui.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.models.NetworkVideo;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnItemClickListener;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnLoadMoreListener;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 1;
    private final int TYPE_PB = 0;

    private List<NetworkVideo> mVideoList;

    private OnItemClickListener mOnItemClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public PlayListAdapter(List<NetworkVideo> videoList, RecyclerView recyclerView) {
        mVideoList = videoList;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    Log.i("loadmoretags", "totalItemCount = " + totalItemCount);
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    Log.i("loadmoretags", "lastVisibleItem = " + lastVisibleItem);
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //if (!loading && (lastVisibleItem - 1) == totalItemCount) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_item_layout, parent, false));
        } else if (viewType == TYPE_PB) {
            return new ProgressBarViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bindVideo(mVideoList.get(position));
        } else if (holder instanceof ItemViewHolder) {
            ((ProgressBarViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mVideoList != null ? mVideoList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mVideoList.get(position) != null ? TYPE_ITEM : TYPE_PB;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_video_picture)
        ImageView mIvPicture;

        @Bind(R.id.tv_video_title)
        TextView mTvTitle;

        @Bind(R.id.tv_video_description)
        TextView mTvDescription;

        @Bind(R.id.tv_video_duration)
        TextView mTvDuration;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        private void bindVideo(NetworkVideo video) {
            Glide.with(mIvPicture.getContext())
                    .load(video.getSnippet().getThumbnails().getVideoPicture().getUrl())
                    .into(mIvPicture);

            mTvTitle.setText(video.getSnippet().getVideoTitle());
            mTvDescription.setText(video.getSnippet().getDescription());
//            mTvDuration.setText(getDuration(video.getContentDetails().getDuration()));
        }

        public String getDuration(String str) {
            String time = str.substring(2);
            long duration = 0L;
            Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
            for (int i = 0; i < indexs.length; i++) {
                int index = time.indexOf((String) indexs[i][0]);
                if (index != -1) {
                    String value = time.substring(0, index);
                    duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                    time = time.substring(value.length() + 1);
                }
            }
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        }

    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar mProgressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.pb_load_more);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }
}