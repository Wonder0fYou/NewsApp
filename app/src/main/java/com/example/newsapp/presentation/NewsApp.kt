package com.example.newsapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.presentation.component.ArticleScreen
import com.example.newsapp.presentation.component.NewsListScreen
import com.example.newsapp.presentation.model.Screen

@Composable
fun NewsApp() {
    val viewModel = hiltViewModel<NewsViewModel>()
    val newsPagingState = viewModel.newsPagingDataFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.NewsListScreen.route) {

        //HomeScreen
        composable(route = Screen.NewsListScreen.route) {
            NewsListScreen(
                newsPagingState = newsPagingState,
                context = context,
                onAction = viewModel::onAction,
                onArticleClick = {
                    navController.navigate(
                        Screen.ArticleScreen.route
                    )
                }
            )
        }

        //ArticleScreen
        composable(route = Screen.ArticleScreen.route) {
            ArticleScreen(
                newsItem = userState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}