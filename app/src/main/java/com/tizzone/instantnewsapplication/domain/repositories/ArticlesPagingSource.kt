package com.tizzone.instantnewsapplication.domain.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper

class ArticlesPagingSource(
    private val service: NewsApi,
    private val mapper: ArticlesItemDtoMapper,
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        TODO("Not yet implemented")
    }
}