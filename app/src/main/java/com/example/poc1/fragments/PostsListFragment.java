package com.example.poc1.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.poc1.R;
import com.example.poc1.adapter.MyPostAdapter;
import com.example.poc1.api.WebAPI;
import com.example.poc1.asynctasks.GetPostsByUserID;
import com.example.poc1.asynctasks.InsertPosts;
import com.example.poc1.models.Post;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsListFragment extends Fragment implements MyPostAdapter.IMyPostItemClick {

    private static final String TAG = "PostsListFragment";
    private PostDisplayCallbacks postDisplayCallbacks;
    private OnPostItemClickCallback itemClickListenerCallback;
    private Call<List<Post>> networkCall;
    private Integer userID;
    private TextView tvNoPosts;
    private String userName;
    private RecyclerView recyclerView;
    private MyPostAdapter myPostAdapter;
    private List<Post> posts;
    private static int scrollToPosition = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean mIsDualPane;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick() called with: view = [" + view + "], position = [" + position + "]");
        if(itemClickListenerCallback!=null && posts!=null && posts.get(position)!=null){
            itemClickListenerCallback.onItemClick(posts.get(position),position);
        }
    }

    public interface OnPostItemClickCallback{
        void onItemClick(Post post,int position);
    }

    public interface PostDisplayCallbacks {
        void onDisplayPost(List<Post> posts);
        void onDisplayPostFail();
    }

    public void setOnItemClickListener(OnPostItemClickCallback callback){
        this.itemClickListenerCallback=callback;
    }


    public PostsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(getActivity() instanceof PostDisplayCallbacks)){
            throw new IllegalStateException("Activity must have to implement postDisplayCallback");
        }
        postDisplayCallbacks = (PostDisplayCallbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (networkCall != null) {
            networkCall.cancel();
        }
        postDisplayCallbacks = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            userID = getArguments().getInt("userID", 1);
            userName = getArguments().getString("userName", "");
            mIsDualPane = getArguments().getBoolean("mIsDualPane", false);
        }

        setActionBar();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        recyclerView = view.findViewById(R.id.rvPost);
        tvNoPosts = view.findViewById(R.id.tvNoPosts);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLatestPosts(userID);
            }
        });


        if (mIsDualPane) {
            layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        } else {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }

        recyclerView.setLayoutManager(layoutManager);

        posts = new LinkedList<>();

        myPostAdapter = new MyPostAdapter(posts);
        myPostAdapter.setItemOnClickListener(this);

        recyclerView.setAdapter(myPostAdapter);

        getLatestPosts(userID);

        return view;
    }

    private void setActionBar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
                actionBar.setTitle(userName);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            if (positions.length > 0) {
                scrollToPosition = positions[0];
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            scrollToPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getLatestPosts(final Integer userID) {
        displayProgressBar(true);
        networkCall = WebAPI.getClient().getPostOfUser(userID);
        networkCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                List<Post> result = null;
                if (response.isSuccessful()){
                    result = response.body();
                }
                displayPost(result);
                displayProgressBar(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                displayPost(null);
                displayProgressBar(false);
            }
        });
    }

    private void displayPost(final List<Post> result) {
        if (result == null) {
            GetPostsByUserID getPostsByUserID = new GetPostsByUserID(getContext());
            getPostsByUserID.setPostLoadCallback(new GetPostsByUserID.LoadPostCallback() {
                @Override
                public void onPostLoadSuccessful(List<Post> posts) {
                    displayPost(posts);
                }

                @Override
                public void onPostLoadFail() {
                    Log.d(TAG, "onPostLoadFail() called");
                    if (postDisplayCallbacks != null) {
                        postDisplayCallbacks.onDisplayPostFail();
                    }
                }
            });
            getPostsByUserID.execute(userID);
        } else {
            InsertPosts insertPosts = new InsertPosts(getContext());
            insertPosts.execute(result);
            posts.addAll(result);
            myPostAdapter.notifyDataSetChanged();
            if (scrollToPosition < posts.size()) {
                layoutManager.scrollToPosition(scrollToPosition);
            }
            if (postDisplayCallbacks != null) {
                if (result.size() > 0) {
                    Log.d(TAG, "displayPost() called with: result = [" + result.size() + "]");
                    postDisplayCallbacks.onDisplayPost(posts);
                } else {
                    postDisplayCallbacks.onDisplayPostFail();
                }
            }
        }
    }

    private void displayProgressBar(boolean isVisible) {
        if (isVisible) {
            recyclerView.setVisibility(View.GONE);
            tvNoPosts.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvNoPosts.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
