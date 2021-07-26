package com.tizzone.instantnewsapplication.presentation

import androidx.lifecycle.lifecycleScope
import androidx.paging.ItemSnapshotList
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
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
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
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
import javax.inject.Inject
import javax.inject.Singleton


@UninstallModules(UrlProvideModule::class)

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticlesRecyclerViewAdapter
    private var list: ItemSnapshotList<Article>? = null

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var okHttp: OkHttpClient

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
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                okHttp
            )
        )
    }

    @Test
    fun areGetAllHeadlinesDataAreSubmitInAdapter() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //List should be empty
        assert(list.isNullOrEmpty())
        //When use case data with 34 articles are submit to PagingDataAdapter
        activityScenario.onActivity { activity ->
            activity.lifecycleScope.launch {
                recyclerView = activity.findViewById(R.id.articles_list)
                adapter = recyclerView.adapter as ArticlesRecyclerViewAdapter
                adapter.loadStateFlow.distinctUntilChangedBy {
                    it.refresh
                }.collect {
                    list = adapter.snapshot()
                }
            }
        }
        //List should be equal to 34
        assert(list?.size == 34)
        activityScenario.close()
    }

    @Test
    fun isRecyclerViewDisplayArticleItems() {
        //Check text for no data is visible
        onView(withId(R.id.no_data_text)).check(
            matches(
                ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE
                )
            )
        )

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
            //Check RecyclerView is visibility visible
            onView(withId(R.id.articles_list)).check(
                matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
            //Check text for no data is not visible
            onView(withId(R.id.no_data_text)).check(
                matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.GONE
                    )
                )
            )
            //Check RecyclerView is displayed
            onView(withId(R.id.articles_list)).check(matches(ViewMatchers.isDisplayed()))
            //Click on first item of the list
            onView(withId(R.id.articles_list))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<ArticlesRecyclerViewAdapter.ArticleViewHolder>(
                        0,
                        ViewActions.click()
                    )
                )
            //Check the article's detail is show
            //Title should be the same as the first item in the list
            //Description should be the same as the first item in the list
            onView(withId(R.id.article_detail_title_text))
                .check(matches(withText("JOendirect:ManonBrunetdécrochelebronzeausabre,laFranceterminelajournéeà5médailles-LeFigaro")))
            onView(withId(R.id.article_detail_description_text))
                .check(matches(withText("DIRECT-SuivezendirectlatroisièmejournéedesJeuxolympiquesdeTokyo.")))

        activityScenario.close()
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
        fun provideUrl(): String = "http://127.0.0.1:8080/"
    }
}