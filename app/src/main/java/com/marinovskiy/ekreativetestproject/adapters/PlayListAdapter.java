package com.marinovskiy.ekreativetestproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.interfaces.OnItemClickListener;
import com.marinovskiy.ekreativetestproject.managers.Utils;
import com.marinovskiy.ekreativetestproject.models.db.VideoParcelable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 1;
    private final int TYPE_PB = 0;

    private List<VideoParcelable> mVideoParcelableList;

    private OnItemClickListener mOnItemClickListener;

    public PlayListAdapter(List<VideoParcelable> videoParcelableList) {
        mVideoParcelableList = videoParcelableList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.playlist_item_layout, parent, false));
            case TYPE_PB:
                return new ProgressBarViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.progress_bar_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bindVideo(mVideoParcelableList.get(position));
        } else {
            ((ProgressBarViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mVideoParcelableList != null ? mVideoParcelableList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mVideoParcelableList.get(position) != null ? TYPE_ITEM : TYPE_PB;
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

        private void bindVideo(VideoParcelable videoParcelable) {
            Glide.with(mIvPicture.getContext())
                    .load(videoParcelable.getPictureUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mIvPicture);

            mTvTitle.setText(videoParcelable.getTitle());
            mTvDescription.setText(videoParcelable.getDescription());
            mTvDuration.setText(Utils.getDuration(videoParcelable.getDuration()));
        }
    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.pb_load_more)
        ProgressBar mProgressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}