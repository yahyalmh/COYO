package com.coyo.codechallenge.data.db.dao

import androidx.room.*
import com.coyo.codechallenge.data.model.Comment


@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArray(vararg comments: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comments: Comment)

    @Update
    suspend fun update(comment: Comment)

    @Delete(entity = Comment::class)
    suspend fun delete(comments: Comment)

    @Query("select * from comments where id=:commentsId")
    suspend fun getComment(commentsId: Int): Comment

    @Query("SELECT count(*) FROM comments")
    fun getCount(): Int
}