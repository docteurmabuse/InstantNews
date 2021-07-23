package com.tizzone.instantnewsapplication.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItemDto>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable