package com.codepath.flicks.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;

public class PopularMovieHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvTitle;
    TextView tvOverview;

    public PopularMovieHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
    }

    public ImageView getIvImage() {
        return ivImage;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvOverview() {
        return tvOverview;
    }
}
