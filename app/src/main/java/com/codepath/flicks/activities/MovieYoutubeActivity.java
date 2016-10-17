package com.codepath.flicks.activities;

import android.content.Intent;
import android.os.Bundle;

import com.codepath.flicks.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieYoutubeActivity extends YouTubeBaseActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_youtube);
        Intent intent = getIntent();
        final Long movieId = intent.getLongExtra("movie_id", -1);
        if (movieId != -1) {
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.myplayer);
            youTubePlayerView.initialize(getString(R.string.youtube_api_key),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            final YouTubePlayer youTubePlayer, boolean b) {

                            String url = String.format("https://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movieId.toString());
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    final String responseData = response.body().string();
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        JSONArray youtubeArray = jsonObject.getJSONArray("youtube");
                                        if (youtubeArray.length() > 0) {
                                            JSONObject y = youtubeArray.getJSONObject(0);
                                            youTubePlayer.loadVideo(y.getString("source"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call call, IOException e) {

                                }
                            });
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                        }
                    });
        } else {
            finish();
        }
    }
}