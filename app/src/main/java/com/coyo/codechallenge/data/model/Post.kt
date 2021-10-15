package com.coyo.codechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "posts")
data class Post(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @SerializedName("userId")
    @ColumnInfo(name = "userId")
    val userId: String,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,

    @SerializedName("body")
    @ColumnInfo(name = "body")
    val body: String,
) : Serializable // implement serializable to pass this class among fragments