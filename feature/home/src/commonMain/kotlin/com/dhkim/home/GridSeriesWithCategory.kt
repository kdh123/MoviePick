package com.dhkim.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.ui.ContentItem
import com.diamondedge.logging.logging
import kotlinx.coroutines.flow.StateFlow

@Composable
fun GridSeriesWithCategory(series: StateFlow<PagingData<Series>>) {
    val items = series.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key = { "${it.key}_${it.id}" }),
            contentType = items.itemContentType()
        ) { index ->
            val item = items[index]
            if (item != null) {
                logging().info { item.imageUrl }
                ContentItem(series = item)
            }
        }
    }
}
