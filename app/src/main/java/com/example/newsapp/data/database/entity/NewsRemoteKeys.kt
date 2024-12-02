package com.example.newsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity("remote_keys")
data class NewsRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
