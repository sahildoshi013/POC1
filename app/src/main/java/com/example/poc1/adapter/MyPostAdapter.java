package com.example.poc1.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc1.R;
import com.example.poc1.models.Post;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder> {

    private static IMyPostItemClick listener;

    public interface IMyPostItemClick {
        void onItemClick(View view, int adapterPosition);
    }

    private static final String TAG = "MyPostAdapter";

    private final List<Post> posts;

    public MyPostAdapter(List<Post> posts){
        this.posts=posts;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_post, parent, false);
        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvBody.setText(post.getBody());
    }

    public void setItemOnClickListener(IMyPostItemClick listener) {
        MyPostAdapter.listener = listener;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvTitle;
        final TextView tvBody;

        MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvPostTitle);
            tvBody = itemView.findViewById(R.id.tvPostBody);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener!=null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

}
