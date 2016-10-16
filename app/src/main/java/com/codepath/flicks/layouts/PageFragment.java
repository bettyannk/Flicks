package com.codepath.flicks.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flicks.R;
import com.codepath.flicks.adapters.MovieArrayAdapter;
import com.codepath.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private SwipeRefreshLayout swipeContainer;

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    @BindView(R.id.lvMovies) ListView lvItems;

    private Unbinder unbinder;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        unbinder = ButterKnife.bind(this, view);

        swipeContainer = (SwipeRefreshLayout) view;

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = "";
                switch (MainActivity.scrolledPage) {
                    case 0: url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                        break;
                    case 1: url = "https://api.themoviedb.org/3/movie/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                        break;
                    case 2: url = "https://api.themoviedb.org/3/movie/upcoming?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                        break;
                    default: url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                }
                asyncCall(url);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        movies = new ArrayList<>();

        if(mPage == 1) {
            asyncCall("https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed");
        } else if(mPage == 2){
            asyncCall("https://api.themoviedb.org/3/movie/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed");
        } else {
            asyncCall("https://api.themoviedb.org/3/movie/upcoming?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed");
        }

        movieAdapter = new MovieArrayAdapter(PageFragment.this.getContext(), movies);
        lvItems.setAdapter(movieAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), MovieDetails.class);
                i.putExtra("movieObject", movieAdapter.getItem(position));
                startActivity(i);
            }
        });

        return view;
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(responseData);
                            JSONArray movieJsonResults = responseJSON.getJSONArray("results");
                            movies.clear();
                            movies.addAll(Movie.fromJSONArray(movieJsonResults));
                            movieAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeContainer.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}