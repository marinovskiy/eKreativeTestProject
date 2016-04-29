package com.marinovskiy.ekreativetestproject.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.models.NetworkVideo;
import com.marinovskiy.ekreativetestproject.ui.listeners.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<NetworkVideo> mVideoList;

    private OnItemClickListener mOnItemClickListener;

    public PlaylistAdapter(List<NetworkVideo> videoList) {
        mVideoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindVideo(mVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideoList != null ? mVideoList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_video_picture)
        ImageView mIvPicture;

        @Bind(R.id.tv_video_duration)
        TextView mTvDuration;

        @Bind(R.id.tv_video_channel_title)
        TextView mTvChannel;

        @Bind(R.id.tv_video_information)
        TextView mTvInformation;

        public ViewHolder(View itemView) {
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
                    .override(video.getSnippet().getThumbnails().getVideoPicture().getWidth(),
                            video.getSnippet().getThumbnails().getVideoPicture().getHeight())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mIvPicture);

            mTvDuration.setText("4:54");
            mTvChannel.setText(video.getSnippet().getChannelTitle());
            mTvInformation.setText(video.getSnippet().getVideoTitle() + " | " + video.getSnippet().getPublishedDate());
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}