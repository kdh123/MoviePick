package com.dhkim.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.designsystem.Black
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.DarkGray60
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.MoviePickButton
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.RecommendationSeriesScope
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    uiState: HomeUiState
) {
    val homeState = (uiState.displayState as? HomeDisplayState.Contents)?.movies?.let { homeMovieItems ->
        rememberHomeState(homeMovieItems = homeMovieItems)
    } ?: rememberHomeState(homeMovieItems = persistentListOf())

    val backgroundColor by animateColorAsState(
        targetValue = if (homeState.isNotRecommendationSeriesShowing) Black50 else homeState.backgroundColor,
        animationSpec = tween(1_000),
        label = "backgroundColor"
    )

    val backgroundGradientColors = listOf(
        backgroundColor.copy(alpha = 0.85f),
        backgroundColor.copy(alpha = 0.65f),
        Black50
    )

    Box(
        modifier = Modifier
            .background(Brush.verticalGradient(backgroundGradientColors))
            .fillMaxSize()
    ) {
        when (uiState.displayState) {
            HomeDisplayState.Loading -> {

            }

            is HomeDisplayState.Contents -> {
                val movies = uiState.displayState.movies
                ContentsScreen(
                    homeState = homeState,
                    homeMovieItems = movies,
                    listState = homeState.listState,
                )
            }

            is HomeDisplayState.Error -> {

            }
        }

        AnimatedVisibility(
            visible = homeState.showCategory,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Black50)
                    .padding(vertical = 8.dp)
            ) {
                CategoryChips(chipColor = Color.LightGray)
            }
        }
    }
}

@Composable
fun AppBar() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Search),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClick { }
            )
        }
    }
}

@Composable
fun CategoryChips(chipColor: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Chip(
            borderColor = chipColor,
            onClick = {}
        ) {
            Text(
                text = "시리즈",
                color = chipColor,
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        }
        Chip(
            borderColor = chipColor,
            onClick = {}
        ) {
            Text(
                text = "영화",
                color = chipColor,
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ContentsScreen(
    homeState: HomeState,
    listState: LazyListState,
    homeMovieItems: ImmutableList<HomeItem>,
) {
    LazyColumn(
        state = listState
    ) {
        items(items = homeMovieItems, key = { item -> item.group }) { item ->
            when (item.group) {
                HomeMovieGroup.APP_BAR -> {
                    AppBar()
                }

                HomeMovieGroup.CATEGORY -> {
                    CategoryChips(chipColor = homeState.onBackgroundColor)
                }

                HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    if (movies.itemCount > 0) {
                        val series = movies[0] as Movie
                        RecommendationSeries(series = series) {
                            Genre()
                            RecommendationButtons()
                        }
                    }
                }

                HomeMovieGroup.TODAY_TOP_10_MOVIES -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                HomeMovieGroup.NOW_PLAYING_MOVIE -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                HomeMovieGroup.TOP_RATED_MOVIE -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                HomeMovieGroup.AIRING_TODAY_TV -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                HomeMovieGroup.ON_THE_AIR_TV -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                HomeMovieGroup.TOP_RATED_TV -> {
                    val movies = (item as HomeItem.HomeMovieItem).series.collectAsLazyPagingItems()
                    SeriesList(title = item.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationSeriesScope.Genre() {
    val movie = series as Movie

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = movie.genre, key = { it }) {
            Text(
                text = it,
                style = MoviePickTheme.typography.labelMedium,
                color = White,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RecommendationSeriesScope.RecommendationButtons() {
    val movie = series as Movie

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        if (movie.video) {
            MoviePickButton(
                color = White,
                onClick = {},
                modifier = Modifier
                    .weight(1f)
            ) {
                Row {
                    Icon(
                        painter = painterResource(Resources.Icon.Play),
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "티저",
                        style = MoviePickTheme.typography.labelLarge,
                        color = Black,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        MoviePickButton(
            color = DarkGray60,
            onClick = {},
            modifier = Modifier
                .weight(1f)
        ) {
            Row {
                Icon(
                    painter = painterResource(Resources.Icon.Add),
                    tint = White,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                )
                Text(
                    text = "찜",
                    style = MoviePickTheme.typography.labelLarge,
                    color = White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Composable
fun SeriesList(title: String, series: LazyPagingItems<Series>, seriesItem: @Composable (Series) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MoviePickTheme.typography.titleMedium,
            modifier = Modifier
                .padding(8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                count = series.itemCount,
                key = series.itemKey(key = {
                    it.id
                }),
                contentType = series.itemContentType()
            ) { index ->
                val item = series[index]
                if (item != null) {
                    seriesItem(item)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    CoilImage(
        modifier = Modifier
            .clip(RoundedCornerShape(12f))
            .width(108.dp)
            .aspectRatio(7f / 10f),
        imageModel = { movie.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
    )
}

@Composable
fun TvItem(tv: Tv) {
    CoilImage(
        modifier = Modifier
            .clip(RoundedCornerShape(12f))
            .width(108.dp)
            .aspectRatio(7f / 10f),
        imageModel = { tv.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.TvPosterSample)
    )
}