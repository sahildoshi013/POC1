package com.example.poc1.adapter;

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

    IMyPostItemClick listener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent , false);
        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvBody.setText(post.getBody());
    }

    public void setItemOnClickListner(IMyPostItemClick listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvBody;

        public MyPostViewHolder(@NonNull View itemView) {
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
