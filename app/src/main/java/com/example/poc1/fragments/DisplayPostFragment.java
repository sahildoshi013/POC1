package com.example.poc1.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.poc1.R;
import com.example.poc1.adapter.MyPostAdapter;
import com.example.poc1.api.WebAPI;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayPostFragment extends Fragment implements MyPostAdapter.IMyPostItemClick {

    private static final String TAG = "DisplayPostFragment";
    private PostDisplayCallbacks postDisplayCallbacks;
    private OnPostItemClickCallback itemClickListenerCallback;

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

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyPostAdapter myPostAdapter;
    private List<Post> posts;
    private User user;

    public DisplayPostFragment() {
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
        postDisplayCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments()==null){
            throw new IllegalStateException("Invalid invocation put user in arguments");
        }
        user = getArguments().getParcelable("user");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        recyclerView = view.findViewById(R.id.rvPost);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        posts = new LinkedList<>();

        myPostAdapter = new MyPostAdapter(posts);
        myPostAdapter.setItemOnClickListner(this);

        recyclerView.setAdapter(myPostAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLatestPosts(user.getId());
    }

    private void getLatestPosts(Integer userID) {
        WebAPI.getClient().getPostOfUser(userID).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> result = null;
                if (response.isSuccessful()){
                    result = response.body();
                }
                if(result!=null){
                    posts.addAll(result);
                    myPostAdapter.notifyDataSetChanged();
                    postDisplayCallbacks.onDisplayPost(posts);
                }else{
                    postDisplayCallbacks.onDisplayPostFail();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                postDisplayCallbacks.onDisplayPostFail();
            }
        });
    }
}
