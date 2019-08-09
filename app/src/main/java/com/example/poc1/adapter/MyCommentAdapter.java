package com.example.poc1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc1.R;
import com.example.poc1.models.Comment;
import com.example.poc1.models.Post;

import java.util.List;

public class MyCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final List<Object> data;

    public MyCommentAdapter(List<Object> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_post, parent, false);
            return new MyPostAdapter.MyPostViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_comment, parent, false);
            return new MyViewHolder(view);
        }
        throw new RuntimeException("No ViewHolder Found for view type - " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder commentHolder = (MyViewHolder) holder;
            Comment comment = (Comment) data.get(position);
            commentHolder.tvUserName.setText(comment.getName());
            commentHolder.tvUserEmailID.setText(comment.getEmail());
            commentHolder.tvCommentBody.setText(comment.getBody());
        } else if (holder instanceof MyPostAdapter.MyPostViewHolder) {
            MyPostAdapter.MyPostViewHolder postHolder = (MyPostAdapter.MyPostViewHolder) holder;
            Post post = (Post) data.get(position);
            postHolder.tvBody.setText(post.getBody());
            postHolder.tvTitle.setText(post.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUserEmailID;
        private final TextView tvUserName;
        private final TextView tvCommentBody;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmailID = itemView.findViewById(R.id.tvUserEmailID);
            tvCommentBody = itemView.findViewById(R.id.tvCommentBody);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return data.get(position) instanceof Post;
    }

}
