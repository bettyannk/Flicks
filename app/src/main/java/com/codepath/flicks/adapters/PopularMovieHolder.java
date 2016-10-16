package com.codepath.flicks.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;

public class PopularMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView ivImage;
    TextView tvTitle;
    TextView tvOverview;

    public PopularMovieHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
        itemView.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
    }
}
