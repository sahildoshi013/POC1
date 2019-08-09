package com.example.poc1.api;

import com.example.poc1.models.Comment;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebAPIInterface {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("posts")
    Call<List<Post>> getPostOfUser(@Query("userId") Integer userId);

    @GET("comments")
    Call<List<Comment>> getCommentsOfPost(@Query("postId") Integer postId);

}
