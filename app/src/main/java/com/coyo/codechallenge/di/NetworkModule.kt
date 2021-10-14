package com.coyo.codechallenge.di

import com.coyo.codechallenge.api.PlaceholderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApodApi(): PlaceholderApi {
        return PlaceholderApi.create()
    }
}