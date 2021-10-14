package com.coyo.codechallenge.data.db.dao

import androidx.room.*
import com.coyo.codechallenge.data.model.User


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArray(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(user: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete(entity = User::class)
    suspend fun delete(user: User)

    @Query("select * from users where id=:userId")
    suspend fun getUser(userId: Int): User

    @Query("SELECT count(*) FROM users")
    fun getCount(): Int
}