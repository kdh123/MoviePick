package com.dhkim.upcoming

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.ShimmerBrush
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun UpcomingScreen(
    uiState: UpcomingUiState,
    navigateToDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (uiState.displayState) {
            UpcomingDisplayState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = Color.White.copy(alpha = 0.6f),
                    trackColor = MaterialTheme.colorScheme.primary,
                )
            }

            is UpcomingDisplayState.Contents -> {
                ContentScreen(
                    series = uiState.displayState.series,
                    navigateToDetail = navigateToDetail
                )
            }

            is UpcomingDisplayState.Error -> {
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
fun ContentScreen(
    series: ImmutableList<FeaturedSeries>,
    navigateToDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    val pages = listOf("🍿 공개 예정", "🔥 인기")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val indicator = @Composable { tabPositions: List<TabPosition> -> CustomIndicator(tabPositions, selectedTabIndex) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            selectedTabIndex = when (index) {
                in series.indexOfFirst { it.group == FeaturedSeriesGroup.Upcoming }..series.indexOfLast { it.group == FeaturedSeriesGroup.Upcoming } -> {
                    0
                }

                else -> {
                    1
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ScrollableTabRow(
            divider = {},
            edgePadding = 0.dp,
            selectedTabIndex = selectedTabIndex,
            indicator = indicator,
            modifier = Modifier
                .padding(10.dp)
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(36.dp)
                        .run {
                            if (index == selectedTabIndex) {
                                this
                            } else {
                                border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(50))
                            }
                        }
                        .zIndex(6f),
                    text = {
                        Text(
                            text = title,
                            style = MoviePickTheme.typography.labelLarge,
                            color = if (selectedTabIndex == index)
                                MaterialTheme.colorScheme.background
                            else
                                MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    selected = index == selectedTabIndex,
                    onClick = {
                        when (index) {
                            0 -> {
                                scope.launch {
                                    listState.animateScrollToItem(series.indexOfFirst { it.group == FeaturedSeriesGroup.Upcoming })
                                }
                            }

                            else -> {
                                scope.launch {
                                    listState.animateScrollToItem(series.indexOfFirst { it.group == FeaturedSeriesGroup.TopRated })
                                }
                            }
                        }
                    },
                )
            }
        }
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            items(items = series, key = { it.series.id }) {
                val openDate = when (it.series) {
                    is Movie -> it.series.releasedDate
                    is Tv -> it.series.firstAirDate
                    else -> ""
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .clickable {
                            navigateToDetail(
                                when (it.series) {
                                    is Movie -> SeriesType.MOVIE
                                    else -> SeriesType.TV
                                },
                                it.series.id
                            )
                        }
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        CoilImage(
                            imageModel = { it.series.imageUrl },
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1.3f)
                                        .background(brush = ShimmerBrush(targetValue = 1_300f))
                                )
                            },
                            previewPlaceholder = painterResource(Resources.Icon.TvPosterSample),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.3f)
                        )
                        Text(
                            text = it.series.title,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        )
                        Text(
                            text = "$openDate 공개",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                        Text(
                            text = it.series.overview,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomIndicator(tabPositions: List<TabPosition>, position: Int) {
    val transition = updateTransition(position, label = "")
    val indicatorStart by transition.animateDp(label = "") {
        tabPositions[it].left
    }
    val indicatorEnd by transition.animateDp(label = "") {
        tabPositions[it].right
    }

    Box(
        modifier = Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.BottomStart)
            .width(indicatorEnd - indicatorStart)
            .padding(2.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground, RoundedCornerShape(50))
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground), RoundedCornerShape(50))
            .zIndex(1f)
    )
}
