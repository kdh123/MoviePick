package com.dhkim.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.ShimmerBrush
import com.dhkim.core.ui.noRippleClick
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookmarkScreen(
    uiState: BookmarkUiState,
    navigateToDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "내가 찜한 리스트",
                    style = MoviePickTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    ) { paddingValues ->
        when (uiState.displayState) {
            BookmarkDisplayState.Loading -> Unit

            is BookmarkDisplayState.Contents -> {
                Contents(
                    uiState.displayState.series,
                    navigateToDetail = navigateToDetail,
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding())
                )
            }

            is BookmarkDisplayState.Error -> {
                Text(
                    text = uiState.displayState.message,
                    style = MoviePickTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
private fun Contents(
    bookmarks: ImmutableList<SeriesBookmark>,
    navigateToDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (bookmarks.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = "내가 찜한 리스트가 존재하지 않습니다.",
                style = MoviePickTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }

        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(items = bookmarks, key = { it.id }) {
            BookmarkItem(
                series = it,
                onClick = {
                    navigateToDetail(it.seriesType, it.id)
                }
            )
        }
    }
}

@Composable
fun BookmarkItem(
    series: SeriesBookmark,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CoilImage(
            modifier = Modifier
                .clip(RoundedCornerShape(12f))
                .width(108.dp)
                .aspectRatio(7f / 10f)
                .noRippleClick(onClick),
            loading = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12f))
                        .width(108.dp)
                        .aspectRatio(7f / 10f)
                        .background(brush = ShimmerBrush(targetValue = 1_300f))
                )
            },
            imageModel = { series.imageUrl },
            failure = {},
            previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
        )
        Text(
            text = series.title,
            style = MoviePickTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}