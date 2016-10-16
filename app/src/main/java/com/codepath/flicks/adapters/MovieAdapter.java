package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.MovieYoutubeActivity;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;
    private final int POPULAR = 0, UNPOPULAR = 1;

    private Context getContext() {
        return mContext;
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        mMovies = movies;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMovies.get(position).isPopularMovie()) {
            return POPULAR;
        } else {
            return UNPOPULAR;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if(viewType == UNPOPULAR) {
            view = inflater.inflate(R.layout.unpopular_movie, parent, false);
            viewHolder = new UnPopularMovieHolder(view);
        } else {
            view = inflater.inflate(R.layout.popular_movie, parent, false);
            viewHolder = new PopularMovieHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder.getItemViewType() == POPULAR) {
            PopularMovieHolder mh1 = (PopularMovieHolder) viewHolder;
            configurePopularMovieViewHolder(mh1, position);
        } else {
            UnPopularMovieHolder mh2 = (UnPopularMovieHolder) viewHolder;
            configureUnpopularMovieViewHolder(mh2, position);
        }
    }

    private void configurePopularMovieViewHolder(PopularMovieHolder viewHolder, int position) {
        final Movie movie = mMovies.get(position);
        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        String imagePath = movie.getBackdropPath();
        Picasso.with(getContext()).load(imagePath).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).fit().transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage);

        viewHolder.ivImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MovieYoutubeActivity.class);
                intent.putExtra("movie_id", movie.getId());
                getContext().startActivity(intent);
            }
        });
    }

    private void configureUnpopularMovieViewHolder(UnPopularMovieHolder viewHolder, int position) {
        Movie movie = mMovies.get(position);
        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        int orientation = getContext().getResources().getConfiguration().orientation;
        String imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ? movie.getPosterPath() : movie.getBackdropPath();
        Picasso.with(getContext()).load(imagePath).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).fit().transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}