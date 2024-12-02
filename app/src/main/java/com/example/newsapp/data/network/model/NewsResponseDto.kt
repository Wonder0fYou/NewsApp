package com.example.newsapp.data.network.model

import com.example.newsapp.domain.entity.ArticleItem
import com.google.gson.annotations.SerializedName

/**
 * Data class representing a Article item DTO (Data Transfer Object) received from the API.
 */

data class NewsResponseDto(

    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: List<ArticleItem>
)
