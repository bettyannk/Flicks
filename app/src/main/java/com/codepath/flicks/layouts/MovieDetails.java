package com.codepath.flicks.layouts;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetails extends YouTubeBaseActivity {

    @BindView(R.id.tvMDTitle) TextView tvTitle;
    @BindView(R.id.tvMDOverview) TextView tvOverview;
    @BindView(R.id.tvMDReleaseDateValue) TextView tvReleaseDate;
    @BindView(R.id.ivMDImage) ImageView ivImage;
    @BindView(R.id.tvMDRuntimeValue) TextView tvRuntime;
    @BindView(R.id.tvMDGenreValue) TextView tvGenre;
    @BindView(R.id.tvMDVotingAverageValue) RatingBar tvVotingAverage;
    @BindView(R.id.tvMDTotalVotesValue) TextView tvTotalVotes;
    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;
    @BindView(R.id.tvMDPopularityValue) TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        final Movie movie = (Movie) getIntent().getSerializableExtra("movieObject");

        String url = String.format("https://api.themoviedb.org/3/movie/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.getId());
        asyncCall(url);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvTotalVotes.setText(String.valueOf(movie.getVoteCount()));
        tvVotingAverage.setRating(Float.parseFloat(String.valueOf(movie.getVoteAverage())));
        tvPopularity.setText(String.format("%.2f", movie.getPopularity()));
        int orientation = getResources().getConfiguration().orientation;
        String imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ? movie.getPosterPath() : movie.getBackdropPath();
        Picasso.with(this).load(imagePath).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).fit().transform(new RoundedCornersTransformation(10, 10)).into(ivImage);

        youTubePlayerView.initialize("AIzaSyCk70hKeShEmA5EDKGNDDaejcUvdb2pNW0",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        if(null== youTubePlayer) return;
                        String videoUrl = String.format("https://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.getId());
                        if(!b) {
                            asyncVideoCall(videoUrl, youTubePlayer);
                        }
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    private OkHttpClient client = new OkHttpClient();

    public void asyncCall(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                MovieDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            int runtime = jsonObject.getInt("runtime");
                            int hours = runtime / 60; //since both are ints, you get an int
                            int minutes = runtime % 60;
                            tvRuntime.setText(hours + "h " +  minutes + "min");
                            JSONArray genreArray = jsonObject.getJSONArray("genres");
                            String genre = "";
                            for(int i = 0; i < genreArray.length(); i++) {
                                JSONObject genreObject = genreArray.getJSONObject(i);
                                genre += genreObject.getString("name");
                                if(i != genreArray.length() - 1) {
                                    genre += ", ";
                                }
                            }
                            tvGenre.setText(genre);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    public void asyncVideoCall(String url, final YouTubePlayer youTubePlayer) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                MovieDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray youtubeArray = jsonObject.getJSONArray("youtube");
                            if(youtubeArray.length() > 0) {
                                JSONObject y = youtubeArray.getJSONObject(0);
                                youTubePlayer.cueVideo(y.getString("source"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }


}
