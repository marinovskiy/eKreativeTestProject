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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private List<NetworkVideo> mVideoList;

    private OnItemClickListener mOnItemClickListener;

    public PlayListAdapter(List<NetworkVideo> videoList) {
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

            mTvDuration.setText(getDuration(video.getContentDetails().getDuration()));
            mTvChannel.setText(video.getSnippet().getChannelTitle());
            mTvInformation.setText(video.getSnippet().getVideoTitle() + " | " + getViews(video.getStatistics().getViewCount()) + " views");
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

        private String getViews(String str) {
            long views = Long.parseLong(str);
            String result;
            if (views > 999 && views < 1000000) {
                result = String.valueOf(views / 1000) + "K";
            } else if (views > 999999) {
                result = String.valueOf(views / 1000000) + "M";
            } else {
                result = String.valueOf(views);
            }
            return result;
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}