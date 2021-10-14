package com.coyo.codechallenge.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.coyo.codechallenge.data.model.Post


@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArray(vararg posts: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Delete(entity = Post::class)
    suspend fun delete(post: Post)


    @Query("SELECT * FROM posts order by id")
    fun getAll(): PagingSource<Int, Post>

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Query("select * from posts where id=:postId")
    suspend fun getComment(postId: Int): Post

    @Query("SELECT count(*) FROM posts")
    fun getCount(): Int
}