package com.example.newsapp.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.R
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.presentation.NewsViewModel

@Composable
fun NewsListScreen(
    onArticleClick: () -> Unit,
    viewModel: NewsViewModel = hiltViewModel(),
    navController: NavController
) {
    val newsPagingState = viewModel.newsPagingDataFlow.collectAsLazyPagingItems()
    Scaffold (
        topBar = {
            NewsListTopBar()
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        NewsListContent(padding, newsPagingState, viewModel, onArticleClick, navController)
    }
}

@Composable
private fun NewsListContent(
    padding: PaddingValues,
    newsPagingState: LazyPagingItems<ArticleItem>,
    viewModel: NewsViewModel,
    onArticleClick: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val pagingState = newsPagingState.loadState
        if (pagingState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    count = newsPagingState.itemCount,
                    key = { index -> newsPagingState[index]?.id ?: "" }
                ) { index ->
                    val item = newsPagingState[index]
                    if (item != null) {
                        NewsItem(
                            newsItem = item,
                            onAction = viewModel::onAction,
                            onArticleClick = onArticleClick,
                            navController = navController
                        )
                    }
                }
                item {
                    if (newsPagingState.itemCount != 0) {
                        if (pagingState.refresh is LoadState.Error || pagingState.refresh is LoadState.NotLoading) {
                            if (newsPagingState.itemCount != 500) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Text(text = stringResource(id = R.string.internet_error))
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = { newsPagingState.retry() }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.retry),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NewsListTopBar() {
    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.news),
            fontSize = 28.sp
        )
    })
}