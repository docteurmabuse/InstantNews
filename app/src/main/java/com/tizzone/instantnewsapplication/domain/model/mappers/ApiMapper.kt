package com.tizzone.instantnewsapplication.domain.model.mappers

interface ApiMapper<E, D> {
    fun mapToDomainModel(itemDto: E): D
}