package com.example.newsapp.data

import com.example.newsapp.data.database.entity.ArticleEntity
import com.example.newsapp.data.database.entity.SourceEntity
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.domain.entity.SourceItem

fun ArticleEntity.toArticleItem(): ArticleItem {
    return ArticleItem(
        source = source.toSourceItem(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun ArticleItem.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        source = source.toSourceEntity(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun SourceItem.toSourceEntity(): SourceEntity {
    return SourceEntity(
        id = id,
        name = name
    )
}

fun SourceEntity.toSourceItem(): SourceItem {
    return SourceItem(
        id = id,
        name = name
    )
}