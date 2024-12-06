package com.example.newsapp.data.network.api

import androidx.annotation.IntRange
import com.example.newsapp.data.network.model.NewsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for interacting with the News API.
 */

interface NewsApiService {

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") q: String = "ios",
        @Query("from") from: String = "2019-04-00",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
        @Query("page") @IntRange(from = 1, to = 5) page: Int?,
    ): Response<NewsResponseDto>
}