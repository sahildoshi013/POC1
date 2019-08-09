package com.example.poc1.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.poc1.R;
import com.example.poc1.adapter.MyCommentAdapter;
import com.example.poc1.api.WebAPI;
import com.example.poc1.asynctasks.GetComments;
import com.example.poc1.asynctasks.GetPost;
import com.example.poc1.asynctasks.InsertComments;
import com.example.poc1.models.Comment;
import com.example.poc1.models.Post;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment {

    private static final String TAG = "PostDetailFragment";
    private int postID;
    private RecyclerView rvComment;
    private List<Object> data;
    private MyCommentAdapter myCommentAdapter;
    private Call<List<Comment>> networkCall;
    private SwipeRefreshLayout swipeRefreshComment;
    private TextView tvNoComment;
    private LinearLayoutManager linearLayoutManager;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        init(view);
        if (getArguments() != null) {
            postID = getArguments().getInt("postID", 1);

            GetPost getPost = new GetPost(getContext());
            getPost.setPostLoadCallback(new GetPost.LoadPostCallback() {
                @Override
                public void onPostLoadSuccessful(List<Post> posts) {
                    data.add(0, posts.remove(0));
                    myCommentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPostLoadFail() {

                }
            });

            getPost.execute(postID);
        }

        swipeRefreshComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostComments();
            }
        });

        rvComment.setLayoutManager(linearLayoutManager);
        rvComment.setAdapter(myCommentAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        rvComment.addItemDecoration(decoration);

        getPostComments();

        return view;
    }

    private void getPostComments() {
        displayProgressBar(true);
        networkCall = WebAPI.getClient().getCommentsOfPost(postID);
        networkCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                List<Comment> result = null;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        result = response.body();
                    }
                }
                displayComments(result);
                displayProgressBar(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                displayComments(null);
                displayProgressBar(false);
            }
        });
    }

    private void displayComments(List<Comment> result) {
        if (result == null) {
            GetComments getComments = new GetComments(getContext());
            getComments.setCommentLoadCallback(new GetComments.LoadCommentCallback() {
                @Override
                public void onPostLoadSuccessful(List<Comment> comments) {
                    Log.d(TAG, "onPostLoadSuccessful() called with: data = [" + comments + "]");
                    displayComments(comments);
                }

                @Override
                public void onPostLoadFail() {
                    Log.d(TAG, "onPostLoadFail() called");
                }
            });
            getComments.execute(postID);
        } else {
            InsertComments insertComments = new InsertComments(getContext());
            insertComments.execute(result);
            data.addAll(result);
            myCommentAdapter.notifyDataSetChanged();
        }
    }

    private void displayProgressBar(boolean isVisible) {
        if (isVisible) {
            swipeRefreshComment.setRefreshing(true);
            rvComment.setVisibility(View.GONE);
            tvNoComment.setVisibility(View.GONE);
        } else {
            swipeRefreshComment.setRefreshing(false);
            rvComment.setVisibility(View.VISIBLE);
            tvNoComment.setVisibility(View.VISIBLE);
        }
    }

    private void init(View view) {
        rvComment = view.findViewById(R.id.rvPostComments);
        swipeRefreshComment = view.findViewById(R.id.swipeRefreshComments);
        tvNoComment = view.findViewById(R.id.tvNoComments);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        data = new LinkedList<>();
        myCommentAdapter = new MyCommentAdapter(data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (networkCall != null) {
            networkCall.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        if (item.getItemId() == android.R.id.home) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.onBackPressed();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
