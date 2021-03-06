package com.tizzone.instantnewsapplication.domain.usecases.articles_list

import com.tizzone.instantnewsapplication.domain.repositories.ArticlesRepositoryImpl
import javax.inject.Inject

class GetAllHeadlines @Inject constructor(
    private val articlesRepositoryImpl: ArticlesRepositoryImpl
) {
    suspend fun invoke() = articlesRepositoryImpl.getAllHeadlines()
}