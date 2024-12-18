package com.example.newsapp.presentation.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.R
import com.example.newsapp.presentation.NewsViewModel
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    onBackClick:() -> Unit,
    viewmodel: NewsViewModel = hiltViewModel(),
    url: String?
) {
    val newsItem by viewmodel.userState.collectAsState()
    Log.d("ArticleScreen", "${newsItem.openUrl}")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                text = stringResource(id = R.string.article),
                fontSize = 28.sp)},
                navigationIcon = {
                    IconButton(onClick = { onBackClick()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                )
        },
    ) { paddingValues ->
        val urlState = rememberWebViewState(url = url ?: "")
        WebView(
            state = urlState,
            onCreated = { it.settings.javaScriptEnabled = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        )
    }
}