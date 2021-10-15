package com.coyo.codechallenge.api

import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.data.model.User
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceholderApi {
    @GET("posts")
    suspend fun getPosts(
        @Query(pageNumberQueryParameterName) pageNumber: String,
        @Query(pageSizeQueryParameterName) limit: String
    ): List<Post>

    @GET("users/{userId}")
    suspend fun getUser(@Path(value = "userId")userId: String): User

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path(value = "postId")postId: String): List<Comment>

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        private const val pageNumberQueryParameterName = "_page"
        private const val pageSizeQueryParameterName = "_limit"

        fun create(): PlaceholderApi {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceholderApi::class.java)
        }
    }
}