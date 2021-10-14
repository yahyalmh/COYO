package com.coyo.codechallenge.data.repo

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.coyo.codechallenge.R
import com.coyo.codechallenge.api.PlaceholderApi
import com.coyo.codechallenge.data.db.CoyoDatabase
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.util.AndroidUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class IRemoteMediator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placeholderApi: PlaceholderApi,
    private val db: CoyoDatabase
) : RemoteMediator<Int, Post>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
        return try {
            when (loadType) {
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> refresh(state)
                LoadType.APPEND -> append(state)
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun refresh(state: PagingState<Int, Post>): MediatorResult {

        // clear database when want to refresh loaded data
        if (!state.isEmpty()) {
            db.withTransaction {
                db.postDao().deleteAll()
            }
        }

        val count = db.withTransaction {
            db.postDao().getCount()
        }

        if (count == 0) {
            val pageNumber = 1
            val data = try {
                requestPosts(pageNumber, state.config.pageSize)
            } catch (e: HttpException) {
                requestPosts(pageNumber, state.config.pageSize)
            }

            db.withTransaction {
                db.postDao().insertList(data)
            }
        }
        return MediatorResult.Success(endOfPaginationReached = false)
    }

    /**
     * This method try to get new data based on last date in db and add at the end of list
     */
    private suspend fun append(state: PagingState<Int, Post>): MediatorResult {
        val count = db.withTransaction {
            db.postDao().getCount()
        }
        if (count >= 100) {
            throw NoMoreItemAvailableException(context.getString(R.string.NoMoreItemAvailable))
        }
        var pageNumber = 1
        if (count >= state.config.pageSize) {
            pageNumber = (count / state.config.pageSize) + 1
        }

        val data = requestPosts(pageNumber, state.config.pageSize)
        db.withTransaction { db.postDao().insertList(data) }
        return MediatorResult.Success(endOfPaginationReached = false)
    }

    private suspend fun requestPosts(pageNumber: Int, pageSize: Int): List<Post> {
        if (!AndroidUtils.isInternetAvailable(context)) {
            throw InternetNotAvailableException(context.getString(R.string.InternetUnavailable))
        }

        while (true) {
            try {
                return placeholderApi.getPosts(pageNumber.toString(), pageSize.toString())
            } catch (e: SocketTimeoutException) {
                throw e
            }
        }
    }

    class InternetNotAvailableException(override val message: String) : Exception()
    class NoMoreItemAvailableException(override val message: String) : Exception()
}

