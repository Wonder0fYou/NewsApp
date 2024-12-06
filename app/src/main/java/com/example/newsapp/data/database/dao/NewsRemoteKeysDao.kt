package com.example.newsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.database.entity.NewsRemoteKeys

@Dao
interface NewsRemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: String): NewsRemoteKeys

    @Query("SELECT * FROM remote_keys")
    suspend fun allKeys(): List<NewsRemoteKeys>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addALlRemoteKeys(remoteKeys: List<NewsRemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}