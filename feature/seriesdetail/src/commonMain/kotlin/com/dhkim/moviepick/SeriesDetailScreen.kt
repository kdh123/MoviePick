package com.dhkim.moviepick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.ShimmerBrush
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
    bookmarks: ImmutableList<SeriesBookmark>,
    onAction: (SeriesDetailAction) -> Unit,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    var showTabBar by remember { mutableStateOf(false) }
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
            SeriesDetailDisplayState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = Color.White.copy(alpha = 0.6f),
                        trackColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = paddingValues.calculateTopPadding())
                            .width(64.dp)
                            .align(Alignment.Center),
                    )
                }
            }

            is SeriesDetailDisplayState.Contents -> {
                val pages = if (uiState.displayState.isUpcoming) listOf("비디오") else listOf("리뷰", "비디오")
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
                val scrollState = rememberScrollState()
                var informationHeight by remember { mutableStateOf(0) }

                LaunchedEffect(Unit) {
                    snapshotFlow { scrollState.value }
                        .collect {
                            showTabBar = it >= informationHeight
                        }
                }

                BoxWithConstraints {
                    val screenHeight = maxHeight - paddingValues.calculateTopPadding()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = paddingValues.calculateTopPadding())
                            .verticalScroll(state = scrollState)
                    ) {
                        Column(
                            modifier = Modifier
                                .onGloballyPositioned {
                                    informationHeight = it.size.height
                                }
                        ) {
                            uiState.displayState.series.forEach {
                                when (it) {
                                    is SeriesDetailItem.AppBar -> Unit
                                    is SeriesDetailItem.SeriesDetailPoster -> {
                                        Poster(imageUrl = it.imageUrl)
                                    }

                                    is SeriesDetailItem.Information -> {
                                        val isBookmarked = bookmarks.any { bookmark -> bookmark.id == it.series.id }
                                        when (it.seriesType) {
                                            SeriesType.MOVIE -> {
                                                MovieInformation(
                                                    movie = it.series as MovieDetail,
                                                    isBookmarked = isBookmarked,
                                                    onAction = onAction
                                                )
                                            }

                                            SeriesType.TV -> {
                                                TvInformation(
                                                    tv = it.series as TvDetail,
                                                    isBookmarked = isBookmarked,
                                                    onAction = onAction
                                                )
                                            }
                                        }
                                    }

                                    else -> Unit
                                }
                            }
                        }

                        val contentTabItem = uiState.displayState.series.find { it is SeriesDetailItem.ContentTab } as SeriesDetailItem.ContentTab
                        val reviews = contentTabItem.reviews.collectAsLazyPagingItems()
                        ContentTab(
                            pagerState = pagerState,
                            scrollState = scrollState,
                            pages = pages.toImmutableList(),
                            videos = contentTabItem.videos,
                            reviews = reviews,
                            navigateToVideo = navigateToVideo,
                            modifier = Modifier
                                .height(screenHeight)
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
    scrollState: ScrollState,
    pages: ImmutableList<String>,
    videos: ImmutableList<VideoItem>,
    reviews: LazyPagingItems<Review>,
    navigateToVideo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TabBar(pagerState, pages)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .nestedScroll(remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                })
        ) {
            if (pages.size == 1) {
                VideoList(videos, navigateToVideo)
            } else {
                when (it) {
                    0 -> ReviewList(reviews)
                    1 -> VideoList(videos, navigateToVideo)
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
fun ReviewList(reviews: LazyPagingItems<Review>) {
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.profilePath != null) {
                            CoilImage(
                                imageModel = { item.profilePath },
                                failure = { painterResource(Resources.Icon.Profile) },
                                previewPlaceholder = painterResource(Resources.Icon.Profile),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(36.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(Resources.Icon.Profile),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(36.dp)
                            )
                        }

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
                        style = MoviePickTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(start = 42.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoList(
    videos: ImmutableList<VideoItem>,
    navigateToVideo: (String) -> Unit,
) {
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
                    loading = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .width(120.dp)
                                .aspectRatio(1.3f)
                                .background(brush = ShimmerBrush(targetValue = 1_300f))
                        )
                    },
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
    movie: MovieDetail,
    isBookmarked: Boolean,
    onAction: (SeriesDetailAction) -> Unit
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
                style = MoviePickTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "${movie.runtime}분",
                style = MoviePickTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
            Text(
                text = Region.entries.firstOrNull { it.code == movie.country }?.country ?: "",
                style = MoviePickTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "출연",
                textAlign = TextAlign.Center,
                style = MoviePickTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )

            Text(
                text = movie.actors.take(5).joinToString(", "),
                style = MoviePickTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
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

        Column(
            modifier = Modifier
                .width(42.dp)
                .clickable {
                    if (isBookmarked) {
                        onAction(SeriesDetailAction.DeleteBookmark(series = movie, seriesType = SeriesType.MOVIE))
                    } else {
                        onAction(SeriesDetailAction.AddBookmark(series = movie, seriesType = SeriesType.MOVIE))
                    }

                }
        ) {
            Icon(
                painter = if (isBookmarked) painterResource(Resources.Icon.Done) else painterResource(Resources.Icon.Add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = "찜",
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TvInformation(
    tv: TvDetail,
    isBookmarked: Boolean,
    onAction: (SeriesDetailAction) -> Unit
) {
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

        Column(
            modifier = Modifier
                .width(42.dp)
                .clickable {
                    if (isBookmarked) {
                        onAction(SeriesDetailAction.DeleteBookmark(series = tv, seriesType = SeriesType.MOVIE))
                    } else {
                        onAction(SeriesDetailAction.AddBookmark(series = tv, seriesType = SeriesType.MOVIE))
                    }

                }
        ) {
            Icon(
                painter = if (isBookmarked) painterResource(Resources.Icon.Done) else painterResource(Resources.Icon.Add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = "찜",
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Poster(
    imageUrl: String,
) {
    CoilImage(
        imageModel = { imageUrl },
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3f)
                    .background(brush = ShimmerBrush(targetValue = 1_300f))
            )
        },
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
    )
}