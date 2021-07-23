package com.tizzone.instantnewsapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: String,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val url: String?,
) : Parcelable
