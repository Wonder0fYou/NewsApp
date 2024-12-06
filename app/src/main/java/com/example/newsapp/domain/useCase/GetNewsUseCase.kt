package com.example.newsapp.domain.useCase

import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
){
    fun invoke() = newsRepository.getNewsFromMediator()
}