package com.codepath.flicks.activities;

import android.content.Intent;
import android.os.Bundle;

import com.codepath.flicks.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import okhttp3.OkHttpClient;

public class VideosYoutubeActivity extends YouTubeBaseActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_youtube);
        Intent intent = getIntent();
        final String movieId = intent.getStringExtra("movie_id");
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.myplayer);
        youTubePlayerView.initialize(getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(movieId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });
    }
}