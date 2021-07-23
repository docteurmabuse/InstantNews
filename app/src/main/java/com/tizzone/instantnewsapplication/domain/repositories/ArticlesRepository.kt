package com.tizzone.instantnewsapplication.domain.repositories

import com.tizzone.instantnewsapplication.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {
    suspend fun getAllHeadlines(): Flow<List<Article>?>
}