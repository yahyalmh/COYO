package com.coyo.codechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.data.model.User
import com.coyo.codechallenge.data.repo.PostDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postDetailRepository: PostDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Retrieve the post from saved state passed by home fragment
     */
    private val post = savedStateHandle.get<Post>(POST_SAVED_STATE_KEY)!!

    /**
     * get the post's user form repository
     */
    suspend fun user(): LiveData<User> {
        return postDetailRepository.getUser(post.userId)
    }

    /**
     * get the post's comments from repository
     */
    suspend fun comments(): LiveData<List<Comment>> {
        return postDetailRepository.getComments(post.id.toString())
    }

    companion object {
        // A key by which the post will passed to the post detail fragment
        const val POST_SAVED_STATE_KEY = "post"
    }
}