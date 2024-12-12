package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.presentation.component.ArticleScreen
import com.example.newsapp.presentation.component.NewsListScreen

@Composable
fun NewsNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.NewsListScreen.route) {

        //HomeScreen
        composable(route = Screen.NewsListScreen.route) {
            NewsListScreen(
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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}