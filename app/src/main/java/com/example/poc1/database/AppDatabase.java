package com.example.poc1.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.poc1.dao.CommentDAO;
import com.example.poc1.dao.PostDAO;
import com.example.poc1.models.Comment;
import com.example.poc1.models.Post;

@Database(entities = {Post.class, Comment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDAO postDao();

    public abstract CommentDAO commentDao();
}
