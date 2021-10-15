package com.coyo.codechallenge.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coyo.codechallenge.api.PlaceholderApi
import com.coyo.codechallenge.data.db.dao.CommentDao
import com.coyo.codechallenge.data.db.dao.UserDao
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDetailRepository @Inject constructor(
    private val placeholderApi: PlaceholderApi,
    private val commentDao: CommentDao,
    private val userDao: UserDao
) {

    /**
     * try to get user from database, if not exist fetch from api
     * save the api result for next request
     */
    suspend fun getUser(userId: String): LiveData<User> {
        var user = userDao.getUser(userId)
        if (user != null) {
            return MutableLiveData(user)
        }

        user = placeholderApi.getUser(userId)

        // save fetched data to database
        userDao.insert(user)

        return MutableLiveData(user)
    }

    /**
     * try to get post's comments from database, if not exist fetch from api
     * save the api result for next request
     */
    suspend fun getComments(postId: String): LiveData<List<Comment>> {
        var comments = commentDao.getPostComments(postId)
        if (comments != null && comments.isNotEmpty()) {
            return MutableLiveData(comments)
        }

        comments = placeholderApi.getComments(postId)

        // save fetched data to database
        commentDao.insertList(comments)

        return MutableLiveData(comments)
    }

}
