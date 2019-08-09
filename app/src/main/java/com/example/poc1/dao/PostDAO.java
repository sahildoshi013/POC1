package com.example.poc1.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.poc1.models.Post;

import java.util.List;

@Dao
public interface PostDAO {

    @Query("SELECT * FROM post WHERE userId IN (:userIds)")
    List<Post> loadAllByUserId(int userIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Post> posts);

    @Query("select * from post where id in (:postId)")
    Post getPost(int postId);

}
