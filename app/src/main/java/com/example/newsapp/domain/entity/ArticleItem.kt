package com.example.newsapp.domain.entity

data class ArticleItem(
    val id: Int,
    val source: SourceItem?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)


