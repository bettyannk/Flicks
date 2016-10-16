package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    static class ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        int orientation = getContext().getResources().getConfiguration().orientation;
        String imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ? movie.getPosterPath() : movie.getBackdropPath();
        Picasso.with(getContext()).load(imagePath).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).fit().transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage);

        return convertView;
    }
}
