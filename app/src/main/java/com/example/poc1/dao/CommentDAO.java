package com.example.poc1.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.poc1.models.Comment;

import java.util.List;

@Dao
public interface CommentDAO {

    @Query("SELECT * FROM comment WHERE postId IN (:postId)")
    List<Comment> loadAllByPostId(int postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Comment> comments);

}
