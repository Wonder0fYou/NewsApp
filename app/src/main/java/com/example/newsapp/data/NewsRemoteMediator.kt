package com.example.newsapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.data.database.AppDatabase
import com.example.newsapp.data.database.entity.ArticleEntity
import com.example.newsapp.data.database.entity.NewsRemoteKeys
import com.example.newsapp.data.network.api.NewsApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * RemoteMediator loads data from the API and
 * save it in the [AppDatabase]
 * @property newsApi [NewsApiService]
 * @property newsDB [AppDatabase]
 */

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsDB: AppDatabase,
    private val newsApi: NewsApiService
): RemoteMediator<Int, ArticleEntity>() {

    private val newsDao = newsDB.newsDao()
    private val newsRemoteKeysDao = newsDB.newsRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND ->   {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = newsApi.getNews(
                page = currentPage,
                perPage = 10
            )
            val endOfPaginationReached = response.articles.isEmpty()

            val prevPage = if (currentPage == 1 || currentPage == 0) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            newsDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDB.newsDao().clearAll()
                    newsRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.articles.map { articleItem ->
                    NewsRemoteKeys(
                        id = articleItem.title,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                val newsEntities = response.articles.map { article ->
                    article.toArticleEntity()
                }
                newsRemoteKeysDao.addALlRemoteKeys(remoteKeys = keys)
                newsDao.upsertAll(newsEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArticleEntity>
    ):NewsRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                newsRemoteKeysDao.getRemoteKeys(id = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ArticleEntity>
    ): NewsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { articleEntity ->
                newsRemoteKeysDao.getRemoteKeys(id = articleEntity.id.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ArticleEntity>
    ): NewsRemoteKeys? {
        return state.pages.lastOrNull() {it.data.isNotEmpty()}?.data?.lastOrNull()
            ?.let { articleEntity ->
                newsRemoteKeysDao.getRemoteKeys(id = articleEntity.id.toString())
            }
    }
}