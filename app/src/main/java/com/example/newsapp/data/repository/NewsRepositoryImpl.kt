package com.example.newsapp.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsapp.data.NewsRemoteMediator
import com.example.newsapp.data.database.AppDatabase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.network.api.NewsApiService
import com.example.newsapp.data.toArticleItem
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * The repository is part of the data layer.
 * It contains the logic of retrieving Article.
 * Scope of application: lives as long as the application itself. Not recreated during setup
 * @property newsApi [NewsApiService]
 * @property newsDB [AppDatabase]
 */

class NewsRepositoryImpl @Inject constructor(
    private val newsDB: AppDatabase,
    private val newsApi: NewsApiService,
): NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNews(): Flow<PagingData<ArticleItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 5
            ),
            remoteMediator = NewsRemoteMediator(
                newsDB = newsDB,
                newsApi = newsApi
            ),
            pagingSourceFactory = {
                newsDB.newsDao().pagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toArticleItem()
            }
        }
    }
}