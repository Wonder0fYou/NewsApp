package com.example.newsapp.data

import android.util.Log
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

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

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
                            endOfPaginationReached = false
                        )
                    prevPage
                }
                LoadType.APPEND ->   {
                    val nextPage = newsDB.withTransaction {
                        newsRemoteKeysDao.allKeys().lastOrNull()
                    }
                    Log.d("NewsRemote", "${nextPage?.nextPage} Append")
                    nextPage?.nextPage ?: return MediatorResult.Success(true)
                }
            }

            val response = newsApi.getNews(
                page = currentPage
            )
            val endOfPaginationReached = response.body()
                ?.articles?.isEmpty() ?: false

            newsDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDB.newsDao().clearAll()
                    newsRemoteKeysDao.deleteAllRemoteKeys()
                }
                val prevPage = if (currentPage == 1 || currentPage == 0) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1
                Log.d("NewsRemote", "$prevPage PrevPage")
                Log.d("NewsRemote", "$nextPage NextPage")
                val keys = response.body()
                    ?.articles?.map { articleItem ->
                        articleItem!!.toArticleEntity()
                        NewsRemoteKeys(
                            id = articleItem.title.toString(),
                            prevPage = prevPage,
                            nextPage = nextPage
                        )
                    }
                val newsEntities = response.body()
                    ?.articles?.map { articleItem ->
                        articleItem!!.toArticleEntity()
                    }
                if (keys != null) {
                    newsRemoteKeysDao.addALlRemoteKeys(remoteKeys = keys)
                }
                if (newsEntities != null) {
                    newsDao.upsertAll(newsEntities)
                }
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
        return state.pages.lastOrNull { it.data.isNotEmpty()}?.data?.lastOrNull()
            ?.let { articleEntity ->
                newsRemoteKeysDao.getRemoteKeys(id = articleEntity.id.toString())
            }
    }
}