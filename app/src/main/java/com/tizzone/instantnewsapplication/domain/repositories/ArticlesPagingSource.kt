package com.tizzone.instantnewsapplication.domain.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tizzone.instantnewsapplication.data.network.NewsApi
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.model.mappers.ArticlesItemDtoMapper
import com.tizzone.instantnewsapplication.utils.ARTICLES_PAGE_SIZE
import com.tizzone.instantnewsapplication.utils.ARTICLES_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class ArticlesPagingSource(
    private val service: NewsApi,
    private val mapper: ArticlesItemDtoMapper,
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLES_STARTING_PAGE_INDEX
        return try {
            val response = service.getHeadLines(page = position, pageSize = params.loadSize)
            val articles = response.articles
            val nextKey = if (articles.isEmpty()) {
                null
            } else {
                position + (params.loadSize / ARTICLES_PAGE_SIZE)
            }
            LoadResult.Page(
                data = mapper.toDomainArticleList(articles),
                prevKey = if (position == ARTICLES_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}