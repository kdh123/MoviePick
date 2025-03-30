package com.dhkim.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhkim.core.designsystem.Black00
import com.dhkim.core.designsystem.Black10
import com.dhkim.core.designsystem.Black30
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.Black70
import com.dhkim.core.designsystem.Black80
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.CircleCloseButton

@Composable
fun CategoryModal(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onClose: () -> Unit
) {
    val bottomBackgroundColors = listOf(Black00, Black10, Black30, Black50, Black70, Black80)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 84.dp, bottom = 42.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Black70)
        ) {
            items(items = categories, key = { it }) { category ->
                val name = when (category) {
                    is Category.Region -> category.country
                    is Category.MovieGenre -> category.genre
                    is Category.TvGenre ->category.genre
                }

                Text(
                    text = name,
                    style = MoviePickTheme.typography.titleLargeLightGrayBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(category) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Brush.verticalGradient(colors = bottomBackgroundColors))
                .align(Alignment.BottomCenter)
        ) {
            CircleCloseButton(
                backgroundColor = White,
                size = 64,
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

