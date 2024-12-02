package com.example.newsapp.di

import com.example.newsapp.data.database.AppDatabase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.network.api.NewsApiService
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideNewsRepository(
        appDatabase: AppDatabase,
        newsApi: NewsApiService
    ): NewsRepository {
        return NewsRepositoryImpl(
            newsDB = appDatabase,
            newsApi = newsApi
        )
    }
}