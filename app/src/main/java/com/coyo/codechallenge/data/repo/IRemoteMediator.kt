package com.coyo.codechallenge.data.repo

import android.content.Context
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

    /**
     * Refresh data when user swipe and at first time
     */
    private suspend fun refresh(state: PagingState<Int, Post>): MediatorResult {

        // Clear database when want to refresh the loaded data
        if (!state.isEmpty()) {
            db.withTransaction {
                db.postDao().deleteAll()
                db.userDao().deleteAll()
                db.commentDao().deleteAll()
            }
        }

        val count = db.withTransaction {
            db.postDao().getCount()
        }

        // If there is no post in database try to fetch first post's pageo
        if (count == 0) {
            val pageNumber = 1
            val data = try {
                requestPosts(pageNumber, state.config.pageSize)
            } catch (e: HttpException) {
                requestPosts(pageNumber, state.config.pageSize)
            }

            // Save the result to database
            db.withTransaction {
                db.postDao().insertList(data)
            }
        }
        return MediatorResult.Success(endOfPaginationReached = false)
    }

    /**
     * This method try to get new data based on last date in db and add at the end of list
     * There is no post more than 100 posts with this api
     */
    private suspend fun append(state: PagingState<Int, Post>): MediatorResult {
        val count = db.withTransaction {
            db.postDao().getCount()
        }

        // Check if all posts are fetched then throw an exception
        if (count >= 100) {
            throw NoMoreItemAvailableException(context.getString(R.string.NoMoreItemAvailable))
        }

        // Calc page number based on post's count in database
        var pageNumber = 1
        if (count >= state.config.pageSize) {
            pageNumber = (count / state.config.pageSize) + 1
        }

        val data = requestPosts(pageNumber, state.config.pageSize)

        // Save the result in database
        db.withTransaction { db.postDao().insertList(data) }
        return MediatorResult.Success(endOfPaginationReached = false)
    }

    private suspend fun requestPosts(pageNumber: Int, pageSize: Int): List<Post> {
        // Check if the internet is not available throw an exception
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

