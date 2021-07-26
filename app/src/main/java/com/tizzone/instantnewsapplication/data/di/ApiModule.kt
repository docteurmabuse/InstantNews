package com.tizzone.instantnewsapplication.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tizzone.instantnewsapplication.BuildConfig
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import com.tizzone.instantnewsapplication.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun  providesNewsApi(okHttpClient: OkHttpClient, BASE_URL: String):NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesArticlesItemDtoMapper(): ArticlesItemDtoMapper {
        return ArticlesItemDtoMapper()
    }
}