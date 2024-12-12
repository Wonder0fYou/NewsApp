package com.example.newsapp.presentation.component

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.navigation.Screen
import com.example.newsapp.presentation.model.UserAction
import com.example.newsapp.utils.formatTime

@Composable
fun NewsItem(
    newsItem: ArticleItem,
    onAction: (UserAction) -> Unit,
    onArticleClick: () -> Unit,
    navController: NavController
) {
    Card (
        modifier = Modifier
            .clickable {
                newsItem.url?.let { url ->
                    val encodeUrl = Uri.encode(url)
                    navController.navigate(Screen.ArticleScreen.createRoute(encodeUrl))
                }
            }
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (newsItem.urlToImage != null) {
                AsyncImage(
                    model = newsItem.urlToImage,
                    contentDescription = newsItem.author,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = newsItem.title ?: "",
                fontSize = 16.sp,
                fontWeight = W800,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = newsItem.author ?: "",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = newsItem.publishedAt!!.formatTime(),
                    fontSize = 12.sp
                )
            }
        }
    }
}