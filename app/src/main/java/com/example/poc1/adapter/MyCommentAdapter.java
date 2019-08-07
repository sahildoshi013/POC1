package com.example.poc1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc1.R;
import com.example.poc1.models.Comment;

import java.util.List;

public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.MyViewHolder> {

    private final List<Comment> comments;

    public MyCommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvUserName.setText(comment.getName());
        holder.tvUserEmailID.setText(comment.getEmail());
        holder.tvCommentBody.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return comments.size();
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
}
