package com.dhkim.moviepick

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Review
import com.dhkim.common.SeriesType
import com.dhkim.common.Video
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
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
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {

        }
    ) { paddingValues ->
        when (uiState.displayState) {
            SeriesDetailDisplayState.Loading -> {}
            is SeriesDetailDisplayState.Contents -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    val listState = rememberLazyListState()
                    var userScrollEnabled by remember {
                        mutableStateOf(false)
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val action = event.type

                                        when (action) {
                                            PointerEventType.Press -> {
                                            }

                                            PointerEventType.Release -> {
                                                userScrollEnabled = listState.firstVisibleItemIndex >= 3
                                            }

                                            PointerEventType.Move -> {
                                                println("➡️ MOVE")
                                            }
                                        }
                                    }
                                }
                            }
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
                                    val videos = it.videos.toImmutableList()

                                    Column {
                                        ContentTab(
                                            videos = videos,
                                            reviews = reviews,
                                            userScrollEnabled = userScrollEnabled
                                        )
                                    }
                                }
                            }
                        }
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
    videos: List<Video>,
    reviews: LazyPagingItems<Review>,
    userScrollEnabled: Boolean
) {
    val pages = listOf("리뷰", "비디오")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(currentTabPosition = tabPositions[pagerState.currentPage])
                        .fillMaxWidth()
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
        ) {
            when (it) {
                0 -> key("review") {
                    ReviewList(reviews, userScrollEnabled)
                }
                1 -> key("video") {
                    VideoList(videos.toImmutableList())
                }
            }
        }
    }
}



@Composable
fun ReviewList(
    reviews: LazyPagingItems<Review>,
    userScrollEnabled: Boolean,
) {


    LazyColumn(
        userScrollEnabled = userScrollEnabled,
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
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = item.createdAt)
                    }
                    Text(
                        text = item.content
                    )
                }
            }
        }
    }
}

@Composable
fun VideoList(videos: ImmutableList<Video>) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        items(items = videos, key = { it.id }) {
            Row {
                Image(
                    painter = painterResource(Resources.Icon.Play),
                    contentDescription = null
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
            fontSize = 24.sp,
            modifier = Modifier
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = movie.releasedDate,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${movie.runtime}분",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
        }

        Text(
            text = movie.overview,
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
            fontSize = 24.sp,
            modifier = Modifier
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tv.firstAirDate,
                textAlign = TextAlign.Center
            )
            Text(
                text = "시즌 ${tv.numberOfSeasons}개",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp)
            )
        }

        Text(
            text = tv.overview,
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