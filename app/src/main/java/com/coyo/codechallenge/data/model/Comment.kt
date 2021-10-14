package com.coyo.codechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comments")
data class Comment(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @SerializedName("postId")
    @ColumnInfo(name = "postId")
    val postId: String,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,

    @SerializedName("body")
    @ColumnInfo(name = "body")
    val body: String,
)