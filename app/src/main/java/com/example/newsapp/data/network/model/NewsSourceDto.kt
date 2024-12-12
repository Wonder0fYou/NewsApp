package com.example.newsapp.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsSourceDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)
