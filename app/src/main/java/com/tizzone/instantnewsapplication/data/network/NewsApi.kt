package com.tizzone.instantnewsapplication.data.network

import com.tizzone.instantnewsapplication.BuildConfig.NEWS_API_KEY
import com.tizzone.instantnewsapplication.data.network.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country")
        countryCode: String = "fr",
        @Query("page")
        page:Int =1,
        @Query("pageSize")
        pageSize: Int = 10,
        @Query("apiKey")
        newsApiKey: String = NEWS_API_KEY
    ): NewsResponse
}