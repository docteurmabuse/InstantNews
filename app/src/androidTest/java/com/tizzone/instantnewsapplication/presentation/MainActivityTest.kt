package com.tizzone.instantnewsapplication.presentation

import androidx.lifecycle.lifecycleScope
import androidx.paging.ItemSnapshotList
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tizzone.instantnewsapplication.R
import com.tizzone.instantnewsapplication.data.di.UrlProvideModule
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.presentation.ui.adapters.ArticlesRecyclerViewAdapter
import com.tizzone.newsapplication.network.data.MockWebServerResponsesTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import javax.inject.Singleton
import org.hamcrest.MatcherAssert.assertThat


@UninstallModules(UrlProvideModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var list: ItemSnapshotList<Article>

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
        //Init mockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(MockWebServerResponsesTest.headlinesListResponse)
            }
        }
    }

    @Test
    fun areGetAllHeadlinesDataAreSubmitInAdapter() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //List should be empty
        assert(list.isEmpty())
        //When use case data with 34 articles are submit to PagingDataAdapter
        activityScenario.onActivity { activity ->
            activity.lifecycleScope.launch {
               val  recyclerView : RecyclerView= activity.findViewById(R.id.articles_list)
                val adapter = recyclerView.adapter as ArticlesRecyclerViewAdapter
                adapter.loadStateFlow.distinctUntilChangedBy {
                    it.refresh
                }.collect {
                    list = adapter.snapshot()
                }
            }
        }
        //List should be equal to 34
        assert(list.size ==34)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class MockBaseUrlModule {
        @Provides
        @Singleton
        fun provideUrl(): String = "http://127.0.0.1:8080"
    }
}