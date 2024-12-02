package com.example.newsapp.presentation.model

sealed class UserAction {
    data class OnNewsCardClick(
        val openUrl: String? = null
    ): UserAction()
}