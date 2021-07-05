package com.chavhan.youtubeapi.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chavhan.youtubeapi.R;
import com.chavhan.youtubeapi.model.CommentModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by mdmunirhossain on 1/4/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.YoutubeCommentHolder> {

    private final ArrayList<CommentModel> dataSet;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext = null;

    public CommentAdapter(Context mContext, ArrayList<CommentModel> data) {
        this.dataSet = data;
        CommentAdapter.mContext = mContext;
    }

    @NotNull
    @Override
    public YoutubeCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.youtube_comment_layout, parent, false);
        return new YoutubeCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubeCommentHolder holder, int position) {
        TextView textViewName = holder.textViewName;
        TextView feedback = holder.feedback;
        ImageView imageView = holder.imageViewIcon;
        CommentModel object = dataSet.get(position);
        textViewName.setText(object.getTitle());
        feedback.setText(object.getComment());
        try {
            object.getThumbnail();
            if (object.getThumbnail().startsWith("http")) {
                Picasso.with(mContext)
                        .load(object.getThumbnail())
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class YoutubeCommentHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView feedback;
        ImageView imageViewIcon;

        public YoutubeCommentHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.profile_image);
            this.feedback = (TextView) itemView.findViewById(R.id.feedback);

        }

    }

}


