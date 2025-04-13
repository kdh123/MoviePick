package com.dhkim.moviepick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Review
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.MovieDetail
import com.dhkim.domain.tv.model.TvDetail
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@Composable
fun SeriesDetailScreen(
    uiState: SeriesDetailUiState,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val showTabBar by remember { derivedStateOf { listState.firstVisibleItemIndex >= 3 } }
    val seriesInformation = (uiState.displayState as? SeriesDetailDisplayState.Contents)
        ?.series
        ?.firstOrNull { it.group == Group.Information } as? SeriesDetailItem.Information
    val seriesTitle = seriesInformation?.series?.title ?: ""

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .noRippleClick(onBack)
                )
                AnimatedVisibility(
                    visible = showTabBar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = seriesTitle,
                        style = MoviePickTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->
        when (uiState.displayState) {
            SeriesDetailDisplayState.Loading -> {}
            is SeriesDetailDisplayState.Contents -> {
                val pages = if (uiState.displayState.isUpcoming) listOf("비디오") else listOf("리뷰", "비디오")
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
                Box(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding())
                        .fillMaxWidth()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                    ) {
                        items(items = uiState.displayState.series, key = { it.group }) {
                            when (it) {
                                is SeriesDetailItem.AppBar -> {}
                                is SeriesDetailItem.SeriesDetailPoster -> {
                                    Poster(imageUrl = it.imageUrl)
                                }

                                is SeriesDetailItem.Information -> {
                                    when (it.seriesType) {
                                        SeriesType.MOVIE -> MovieInformation(movie = it.series as MovieDetail)
                                        SeriesType.TV -> TvInformation(it.series as TvDetail)
                                    }
                                }

                                is SeriesDetailItem.ContentTab -> {
                                    val reviews = it.reviews.collectAsLazyPagingItems()
                                    ContentTab(
                                        pagerState = pagerState,
                                        pages = pages.toImmutableList(),
                                        videos = it.videos,
                                        reviews = reviews,
                                        showTabBar = showTabBar,
                                        navigateToVideo = navigateToVideo
                                    )
                                }
                            }
                        }
                    }
                    if (showTabBar) {
                        TabBar(
                            pagerState = pagerState,
                            pages = pages.toImmutableList(),
                            modifier = Modifier
                        )
                    }
                }
            }

            is SeriesDetailDisplayState.Error -> {

            }
        }
    }
}


@Composable
fun ContentTab(
    pagerState: PagerState,
    pages: ImmutableList<String>,
    videos: ImmutableList<VideoItem>,
    reviews: LazyPagingItems<Review>,
    showTabBar: Boolean,
    navigateToVideo: (String) -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        TabBar(pagerState, pages)
        HorizontalPager(state = pagerState) {
            if (pages.size == 1) {
                VideoList(videos, showTabBar, navigateToVideo)
            } else {
                when (it) {
                    0 -> ReviewList(reviews, showTabBar)
                    1 -> VideoList(videos, showTabBar, navigateToVideo)
                }
            }
        }
    }
}

@Composable
fun TabBar(
    pagerState: PagerState,
    pages: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier
            .fillMaxWidth()
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        style = MoviePickTheme.typography.labelMedium,
                        color = if (pagerState.currentPage == index) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@Composable
fun ReviewList(
    reviews: LazyPagingItems<Review>,
    showTabBar: Boolean,
) {
    val listState = rememberLazyListState()
    val isTopAt by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
    }

    if (reviews.itemCount <= 0) {
        Text(
            text = "리뷰가 존재하지 않습니다.",
            style = MoviePickTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp)
                .padding(10.dp)
        )
        return
    }

    LazyColumn(
        state = listState,
        userScrollEnabled = listState.isScrollInProgress || !isTopAt || showTabBar,
        modifier = Modifier
            .fillMaxWidth()
            .height(900.dp)
    ) {
        items(
            count = reviews.itemCount,
            key = reviews.itemKey(key = { it.id }),
            contentType = reviews.itemContentType()
        ) { index ->
            val item = reviews[index]
            if (item != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = item.author,
                            style = MoviePickTheme.typography.bodyMediumBold
                        )
                        Text(
                            text = item.createdAt,
                            style = MoviePickTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = item.content,
                        style = MoviePickTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun VideoList(
    videos: ImmutableList<VideoItem>,
    showTabBar: Boolean,
    navigateToVideo: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val isTopAt by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
    }

    if (videos.isEmpty()) {
        Text(
            text = "동영상이 존재하지 않습니다.",
            style = MoviePickTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp)
                .padding(10.dp)
        )
        return
    }

    LazyColumn(
        state = listState,
        userScrollEnabled = listState.isScrollInProgress || !isTopAt || showTabBar,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(900.dp)
    ) {
        items(items = videos, key = { it.id }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClick { navigateToVideo(it.key) }
            ) {
                CoilImage(
                    imageModel = { it.thumbnail },
                    previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .width(120.dp)
                        .aspectRatio(1.3f)
                )
                Text(
                    text = it.type.type,
                    style = MoviePickTheme.typography.titleMediumBold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun MovieInformation(
    movie: MovieDetail
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = movie.title,
            textAlign = TextAlign.Center,
            style = MoviePickTheme.typography.bodyLargeBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = movie.openDate,
                textAlign = TextAlign.Center,
                style = MoviePickTheme.typography.bodyMedium
            )
            Text(
                text = "${movie.runtime}분",
                style = MoviePickTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
        }

        Text(
            text = movie.overview,
            style = MoviePickTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun TvInformation(tv: TvDetail) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = tv.title,
            textAlign = TextAlign.Center,
            style = MoviePickTheme.typography.bodyLargeBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tv.openDate,
                textAlign = TextAlign.Center,
                style = MoviePickTheme.typography.bodyMedium,
            )
            Text(
                text = "시즌 ${tv.numberOfSeasons}개",
                style = MoviePickTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
        }

        Text(
            text = tv.overview,
            style = MoviePickTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun Poster(
    imageUrl: String,
) {
    CoilImage(
        imageModel = { imageUrl },
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
    )
}