package com.dhkim.home.series

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.ContentItem
import com.diamondedge.logging.logging

@Composable
fun SeriesCollectionScreen(
    uiState: SeriesCollectionUiState,
    onBack: () -> Unit
) {
    when (uiState.displayState) {
        is SeriesCollectionDisplayState.Contents -> {
            val series = uiState.displayState.series.collectAsLazyPagingItems()
            SeriesCollectionContent(
                category = uiState.categoryName,
                series = series,
                onBack = onBack
            )
        }

        is SeriesCollectionDisplayState.Error -> {

        }

        SeriesCollectionDisplayState.Loading -> {

        }
    }
}

@Composable
fun SeriesCollectionContent(
    category: String,
    series: LazyPagingItems<Series>,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .clickable(onClick = onBack)
            )

            Text(
                text = category,
                style = MoviePickTheme.typography.titleMediumBold
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
        ) {
            items(
                count = series.itemCount,
                key = series.itemKey(key = { "${it.key}_${it.id}" }),
                contentType = series.itemContentType()
            ) { index ->
                val item = series[index]
                if (item != null) {
                    logging().info { item.imageUrl }
                    ContentItem(series = item)
                }
            }
        }
    }
}