package com.example.newsapp.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Upsert
import com.example.newsapp.data.database.entity.ArticleEntity
import androidx.room.Query

/**
 * Data Access Object (DAO) for accessing and manipulating [ArticleEntity] in the database.
 */

@Dao
interface NewsDao {

    @Upsert
    suspend fun upsertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}