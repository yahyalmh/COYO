package com.coyo.codechallenge.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coyo.codechallenge.data.db.dao.CommentDao
import com.coyo.codechallenge.data.db.dao.PostDao
import com.coyo.codechallenge.data.db.dao.UserDao
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.data.model.User

@Database(
    entities = [User::class, Post::class, Comment::class],
    version = 1,
    exportSchema = false
)
abstract class CoyoDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
}