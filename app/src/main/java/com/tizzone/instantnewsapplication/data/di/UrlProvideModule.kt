package com.tizzone.instantnewsapplication.data.di

import android.provider.SyncStateContract
import com.tizzone.instantnewsapplication.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UrlProvideModule {
    @Provides
    fun provideBaseUrl() = BASE_URL
}