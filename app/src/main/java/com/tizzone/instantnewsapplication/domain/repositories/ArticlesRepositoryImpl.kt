package com.tizzone.instantnewsapplication.domain.repositories

import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val mapper: ArticlesItemDtoMapper,
) : ArticlesRepository {
    override suspend fun getAllHeadlines(): Flow<List<Article>?> = flow {
        try {
            val networkArticles = newsApi.getHeadLines().articles
            val articlesList = networkArticles?.let { mapper.toDomainArticleList(it) }
            emit(articlesList)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }
}