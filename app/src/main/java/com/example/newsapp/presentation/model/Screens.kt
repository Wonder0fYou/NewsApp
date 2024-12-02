package com.example.newsapp.presentation.model

sealed class Screen(
    val route: String
) {
    data object NewsListScreen: Screen(
        route = "homeScreen"
    )
    data object ArticleScreen: Screen(
        route = "articleScreen"
    )
}