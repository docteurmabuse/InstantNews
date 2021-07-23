package com.tizzone.instantnewsapplication.data.network

import com.tizzone.instantnewsapplication.BuildConfig.NEWS_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country")
        countryCode: String = "fr",
        @Query("apiKey")
        newsApiKey: String = NEWS_API_KEY
    )
}