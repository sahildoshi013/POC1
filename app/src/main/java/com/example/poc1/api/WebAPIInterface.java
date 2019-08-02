package com.example.poc1.api;

import com.example.poc1.models.Comment;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebAPIInterface {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{user_id}/posts")
    Call<List<Post>> getPostOfUser(@Path("user_id") Integer userId);

    @GET("posts/{post_id}/comments")
    Call<List<Comment>> getCommentsOfPost(@Path("post_id") Integer postId);

}
