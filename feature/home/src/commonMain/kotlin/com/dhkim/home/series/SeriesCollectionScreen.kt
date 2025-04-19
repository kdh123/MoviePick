package com.dhkim.home.series

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.ShimmerBrush
import com.dhkim.domain.movie.model.Movie
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun SeriesCollectionScreen(
    uiState: SeriesCollectionUiState,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (uiState.displayState) {
            SeriesCollectionDisplayState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = Color.White.copy(alpha = 0.6f),
                    trackColor = MaterialTheme.colorScheme.primary,
                )
            }

            is SeriesCollectionDisplayState.Contents -> {
                val series = uiState.displayState.series.collectAsLazyPagingItems()
                SeriesCollectionContent(
                    category = uiState.categoryName,
                    series = series,
                    navigateToSeriesDetail = navigateToSeriesDetail,
                    onBack = onBack
                )
            }

            is SeriesCollectionDisplayState.Error -> {
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
fun SeriesCollectionContent(
    category: String,
    series: LazyPagingItems<Series>,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
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
                    CoilImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12f))
                            .aspectRatio(7f / 10f)
                            .clickable {
                                val seriesType = when {
                                    item is Movie -> SeriesType.MOVIE
                                    else -> SeriesType.TV
                                }
                                navigateToSeriesDetail(seriesType, item.id)
                            },
                        loading = {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12f))
                                    .aspectRatio(7f / 10f)
                                    .background(brush = ShimmerBrush(targetValue = 1_300f))
                            )
                        },
                        imageModel = { item.imageUrl },
                        failure = {},
                        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
                    )
                }
            }
        }
    }
}