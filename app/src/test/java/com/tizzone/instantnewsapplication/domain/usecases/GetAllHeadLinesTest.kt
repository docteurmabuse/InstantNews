package com.tizzone.instantnewsapplication.domain.usecases

import com.google.gson.GsonBuilder
import com.tizzone.instantnewsapplication.data.network.MockWebserverResponse.headlinesListResponse
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import com.tizzone.instantnewsapplication.domain.repositories.ArticlesRepositoryImpl
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

        //Instantiate system in test
        articlesRepositoryImpl = ArticlesRepositoryImpl(newsApi, dtoMapper)

        //Instantiate system in test
       // getAllHeadLines = GetAllHeadlines(articlesRepositoryImpl)
    }

    @Test
    fun getAllHeadLinesFromNetwork(): Unit = runBlocking {
        //Condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(headlinesListResponse)
        )

        val articles =  newsApi.getHeadLines().articles.let { dtoMapper.toDomainArticleList(it) }
        assert(articles.isNotEmpty())
        assert(articles.get(index = 0) is Article)

    }

    @AfterEach
    fun tearDown(){
        mockWebServer.shutdown()
    }
}