package com.example.newsapp.data.database.entity

import androidx.room.ColumnInfo

data class SourceEntity(

    @ColumnInfo("sourceId")
    val id: String?,

    @ColumnInfo("name")
    val name: String?
)
