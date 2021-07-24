package com.tizzone.instantnewsapplication.domain.usescases

import com.tizzone.instantnewsapplication.domain.repositories.ArticlesRepositoryImpl
import javax.inject.Inject

class GetAllHeadlines @Inject constructor(
    private val articlesRepositoryImpl: ArticlesRepositoryImpl
) {
    suspend fun invoke() = articlesRepositoryImpl.getAllHeadlines()
}