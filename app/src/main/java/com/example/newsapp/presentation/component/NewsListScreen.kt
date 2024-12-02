package com.example.newsapp.presentation.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.*
import com.example.newsapp.R
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.presentation.model.UserAction
import com.example.newsapp.presentation.model.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    newsPagingState: LazyPagingItems<ArticleItem>,
    context: Context,
    onAction: (UserAction) -> Unit,
    onArticleClick: () -> Unit
) {
    LaunchedEffect(key1 = newsPagingState.loadState) {
        if (newsPagingState.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error " + (newsPagingState.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Scaffold (
        topBar = {
            TopAppBar(title = { Text(
                text = stringResource(id = R.string.news),
                fontSize = 28.sp
            )})
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            if (newsPagingState.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = newsPagingState.itemCount,
                        key = {index -> newsPagingState[index]?.url ?: index}
                    ) { index ->
                        val item = newsPagingState[index]
                        if (item != null) {
                            NewsItem(
                                newsItem = item,
                                onAction = onAction,
                                onArticleClick = onArticleClick
                            )
                        }
                    }
                    item {
                        if (newsPagingState.loadState.append is LoadState.Loading){
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}