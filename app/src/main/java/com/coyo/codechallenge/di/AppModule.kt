package com.coyo.codechallenge.di

import android.content.Context
import androidx.room.Room
import com.coyo.codechallenge.data.db.CoyoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): CoyoDatabase {
        val dbName = "Coyo.db"
        return Room.databaseBuilder(context, CoyoDatabase::class.java, dbName)
//            .addMigrations(*ALL_MIGRATION) // if there is any migration must set this
            .build()
    }

    @Singleton
    @Provides
    fun providePostDao(db: CoyoDatabase) = db.postDao()


    @Singleton
    @Provides
    fun provideUserDao(db: CoyoDatabase) = db.userDao()


    @Singleton
    @Provides
    fun provideCommentDao(db: CoyoDatabase) = db.commentDao()
}