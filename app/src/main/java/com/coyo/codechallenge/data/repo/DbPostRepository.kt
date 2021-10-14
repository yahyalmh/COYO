package com.coyo.codechallenge.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.coyo.codechallenge.api.PlaceholderApi
import com.coyo.codechallenge.data.db.dao.PostDao
import com.coyo.codechallenge.data.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbPostRepository @Inject constructor(
    private val placeholderApi: PlaceholderApi,
    private val postDao: PostDao
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    @ExperimentalPagingApi
    @Inject
    lateinit var remoteMediator: IRemoteMediator

    @OptIn(ExperimentalPagingApi::class)
    fun fetchPostPage(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            remoteMediator = remoteMediator
        ) {
            postDao.getAll()
        }.flow
    }

}
