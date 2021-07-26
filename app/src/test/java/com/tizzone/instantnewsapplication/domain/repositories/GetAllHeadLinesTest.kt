package com.tizzone.instantnewsapplication.domain.repositories

import com.google.gson.GsonBuilder
import com.tizzone.instantnewsapplication.data.network.MockWebserverResponse.headlinesListResponse
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import com.tizzone.instantnewsapplication.domain.usecases.articles_list.GetAllHeadlines
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HttpsURLConnection

class GetAllHeadLinesTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    //System in test
    private lateinit var getAllHeadLines: GetAllHeadlines

    //Dependencies
    private lateinit var newsApi: NewsApi
    private lateinit var articlesRepositoryImpl: ArticlesRepositoryImpl
    private val dtoMapper = ArticlesItemDtoMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("")

        //Instantiate system in test
        newsApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .create()
                )
            )
            .build()
            .create(NewsApi::class.java)

    }

    @Test
    fun testGetHeadLinesFromNetworkIsWorking(): Unit = runBlocking {
        //Condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(headlinesListResponse)
        )
        var articles = listOf<Article>()
        //Article list should be empty
        assert(articles.isEmpty())
        //Adding api result of articles in the list
        articles = newsApi.getHeadLines().articles.let { dtoMapper.toDomainArticleList(it) }
        //Article lilst should not be empty
        assert(articles.isNotEmpty())
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}