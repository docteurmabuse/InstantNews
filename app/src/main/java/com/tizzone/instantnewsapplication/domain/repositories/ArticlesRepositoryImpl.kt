package com.tizzone.instantnewsapplication.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import com.tizzone.instantnewsapplication.utils.ARTICLES_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val mapper: ArticlesItemDtoMapper,
) : ArticlesRepository {
    override suspend fun getAllHeadlines(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = ARTICLES_PAGE_SIZE),
            pagingSourceFactory = { ArticlesPagingSource(newsApi, mapper) }
        ).flow
    }
}