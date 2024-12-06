package com.example.newsapp.domain.repository

import androidx.paging.PagingData
import com.example.newsapp.domain.entity.ArticleItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsFromNetwork(): Flow<PagingData<ArticleItem>>
    fun getNewsFromMediator(): Flow<PagingData<ArticleItem>>
}