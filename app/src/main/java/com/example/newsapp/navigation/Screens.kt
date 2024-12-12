package com.example.newsapp.navigation

sealed class Screen(
    val route: String
) {
    data object NewsListScreen: Screen(
        route = "homeScreen"
    )
    data object ArticleScreen: Screen(
        route = "articleScreen/{url}"
    ) {
        fun createRoute(url: String): String = "articleScreen/$url"
    }
}