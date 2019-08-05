package com.example.poc1.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc1.R;
import com.example.poc1.adapter.MyCommentAdapter;
import com.example.poc1.api.WebAPI;
import com.example.poc1.asynctasks.GetComments;
import com.example.poc1.asynctasks.InsertComments;
import com.example.poc1.models.Comment;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    private static final String TAG = "PostDetailsFragment";
    private String title;
    private String body;
    private int postID;
    private TextView tvBody;
    private TextView tvTitle;
    private RecyclerView rvComment;
    private List<Comment> comments;
    private MyCommentAdapter myCommentAdapter;
    private Call<List<Comment>> networkCall;
    private ProgressBar progressBarComment;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        initViews(view);
        if (getArguments() != null) {
            title = getArguments().getString("postTitle", "Fail");
            body = getArguments().getString("postBody", "Fail");
            postID = getArguments().getInt("postID", 1);

            tvTitle.setText(title);
            tvBody.setText(body);
        }

        rvComment = view.findViewById(R.id.rvPostComments);
        progressBarComment = view.findViewById(R.id.progressBarComment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvComment.setLayoutManager(linearLayoutManager);

        comments = new LinkedList<>();

        myCommentAdapter = new MyCommentAdapter(comments);
        rvComment.setAdapter(myCommentAdapter);

        getPostComments();

        return view;
    }

    private void getPostComments() {
        displayProgressBar(true);
        networkCall = WebAPI.getClient().getCommentsOfPost(postID);
        networkCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
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
            public void onFailure(Call<List<Comment>> call, Throwable t) {
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
                    Log.d(TAG, "onPostLoadSuccessful() called with: comments = [" + comments + "]");
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
            comments.addAll(result);
            myCommentAdapter.notifyDataSetChanged();
        }
    }

    private void displayProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBarComment.setVisibility(View.VISIBLE);
            rvComment.setVisibility(View.GONE);
        } else {
            progressBarComment.setVisibility(View.GONE);
            rvComment.setVisibility(View.VISIBLE);
        }
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvBody = view.findViewById(R.id.tvBody);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (networkCall != null) {
            networkCall.cancel();
        }
    }
}
