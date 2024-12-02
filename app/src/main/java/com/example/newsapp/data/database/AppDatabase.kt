package com.example.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.database.dao.NewsRemoteKeysDao
import com.example.newsapp.data.database.entity.ArticleEntity
import com.example.newsapp.data.database.entity.NewsRemoteKeys

/**
 * The Room database class for the application.
 * @property newsDao The Data Access Object (DAO) for accessing [ArticleEntity] in the database.
 */

@Database(
    entities = [ArticleEntity::class,
               NewsRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun newsRemoteKeysDao(): NewsRemoteKeysDao
}