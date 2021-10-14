package com.coyo.codechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,

    @SerializedName("username")
    @ColumnInfo(name = "username")
    val username: String,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,

    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    val phone: String,

    @SerializedName("website")
    @ColumnInfo(name = "website")
    val website: String,

    @Embedded
    @SerializedName("company")
    val company: Company,

    @Embedded
    @SerializedName("address")
    val address: Address,
)

data class Company(
    @SerializedName("name")
    @ColumnInfo(name = "companyName")
    val name: String,

    @SerializedName("catchPhrase")
    @ColumnInfo(name = "catchPhrase")
    val catchPhrase: String,

    @SerializedName("bs")
    @ColumnInfo(name = "bs")
    val bs: String,
)

data class Address(
    @SerializedName("street")
    @ColumnInfo(name = "street")
    val street: String,

    @SerializedName("suite")
    @ColumnInfo(name = "suite")
    val suite: String,

    @SerializedName("city")
    @ColumnInfo(name = "city")
    val city: String,

    @SerializedName("zipcode")
    @ColumnInfo(name = "zipcode")
    val zipcode: String,

    @Embedded
    @SerializedName("geo")
    val geo: Geo,
)

data class Geo(
    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    val lat: String,

    @SerializedName("lng")
    @ColumnInfo(name = "lng")
    val lng: String,
)