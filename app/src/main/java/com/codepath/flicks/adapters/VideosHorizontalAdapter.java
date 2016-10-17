package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.VideosYoutubeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideosHorizontalAdapter extends RecyclerView.Adapter<VideosHorizontalAdapter.MyViewHolder> {

    private List<String> horizontalList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView txtView;

        public MyViewHolder(View view) {
            super(view);
            txtView = (ImageView) view.findViewById(R.id.txtView);
        }
    }

    public VideosHorizontalAdapter(Context context, List<String> horizontalList) {
        this.horizontalList = horizontalList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String vUrl = horizontalList.get(position);
        vUrl = String.format("https://img.youtube.com/vi/%s/0.jpg", vUrl);
        Picasso.with(mContext).load(vUrl).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).fit().into(holder.txtView);
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideosYoutubeActivity.class);
                intent.putExtra("movie_id", horizontalList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}