package com.marinovskiy.ekreativetestproject.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.marinovskiy.ekreativetestproject.R;
import com.marinovskiy.ekreativetestproject.api.youtube.YoutubeApiConstants;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayVideoActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {

    public static final String INTENT_KEY_VIDEO_ID = "intent_key_video_id";

    private static final int RECOVERY_REQUEST = 1;

    @Bind(R.id.youtube_view)
    YouTubePlayerView mYoutubeView;

    private String mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mVideoId = getIntent().getStringExtra(INTENT_KEY_VIDEO_ID);
        }
        mYoutubeView.initialize(YoutubeApiConstants.API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.setFullscreen(true);
            youTubePlayer.cueVideo(mVideoId);
            youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        if (error.isUserRecoverableError()) {
            error.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Toast.makeText(this, String.format(Locale.getDefault(), "Error: %s", error.toString()), // TODO STRINGS
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            mYoutubeView.initialize(YoutubeApiConstants.API_KEY, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
