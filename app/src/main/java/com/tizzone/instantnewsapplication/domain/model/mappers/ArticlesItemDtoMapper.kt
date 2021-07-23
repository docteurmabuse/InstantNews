package com.tizzone.instantnewsapplication.domain.model.mappers

import com.tizzone.instantnewsapplication.data.network.model.ArticlesItemDto
import com.tizzone.instantnewsapplication.domain.model.Article
import java.util.*

class ArticlesItemDtoMapper() : ApiMapper<ArticlesItemDto, Article> {
    override fun mapToDomainModel(itemDto: ArticlesItemDto): Article {
        return Article(
            id = UUID.randomUUID().toString(),
            title = itemDto.title,
            description = itemDto.description,
            imageUrl = itemDto.urlToImage,
            url = itemDto.url,
        )
    }

    fun toDomainArticleList(initial: List<ArticlesItemDto>): List<Article> {
        return initial.map { mapToDomainModel(it) }
    }
}