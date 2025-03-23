package com.dhkim.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.Black70
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.MovieItem
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.SeriesList
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@ExperimentalSharedTransitionApi
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToMovie: () -> Unit
) {
    val homeState = (uiState.displayState as? HomeDisplayState.Contents)?.movies?.let { homeMovieItems ->
        rememberHomeState(seriesItems = homeMovieItems, mainRecommendationMovieGroup = Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE)
    } ?: rememberHomeState(seriesItems = persistentListOf(), mainRecommendationMovieGroup = Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE)

    val backgroundGradientColors = listOf(
        homeState.backgroundColor,
        Black50
    )

    val onBackgroundColor by animateColorAsState(
        targetValue = if (homeState.showCategory) White else homeState.onBackgroundColor,
        animationSpec = tween(500),
        label = ""
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
                    homeSeriesItems = movies,
                    listState = homeState.listState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAction = onAction,
                    onUpdateRecommendationSeriesHeight = homeState::updateHeight,
                    navigateToVideo = navigateToVideo,
                    navigateToMovie = navigateToMovie
                )
            }

            is HomeDisplayState.Error -> {

            }
        }

        if (homeState.showTab) {
            Box(
                modifier = Modifier
                    .background(color = Black70)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = homeState.backgroundColor)
                ) {
                    HomeCategoryChips(
                        chipKey = "movie-category",
                        chipColor = onBackgroundColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onAction = {},
                        navigateToMovie = navigateToMovie
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBar() {
    Row {
        Text(
            text = "MOVIK",
            style = MoviePickTheme.typography.headlineLargeBold,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
        )
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun HomeCategoryChips(
    chipColor: Color,
    chipKey: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    navigateToMovie: () -> Unit
) {
    with(sharedTransitionScope) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Chip(
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(key = chipKey),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .noRippleClick { navigateToMovie() },
                borderColor = chipColor,
            ) {
                Text(
                    text = "영화",
                    color = chipColor,
                    style = MoviePickTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                )
            }

            Chip(
                borderColor = chipColor,
            ) {
                Text(
                    text = "TV 프로그램",
                    color = chipColor,
                    style = MoviePickTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                )
            }
            Chip(
                borderColor = chipColor,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "카테고리",
                        color = chipColor,
                        style = MoviePickTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        painter = painterResource(Resources.Icon.DropDown),
                        contentDescription = null,
                        tint = chipColor
                    )
                }
            }
        }
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun ContentsScreen(
    homeState: HomeState,
    listState: LazyListState,
    homeSeriesItems: ImmutableList<SeriesItem>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    onUpdateRecommendationSeriesHeight: (Int) -> Unit,
    navigateToMovie: () -> Unit,
    navigateToVideo: (String) -> Unit,
) {
    LazyColumn(
        state = listState,
    ) {
        items(items = homeSeriesItems, key = { item -> item.group }) { item ->
            when (item.group as Group.HomeGroup) {
                Group.HomeGroup.APP_BAR -> {
                    AppBar()
                }

                Group.HomeGroup.CATEGORY -> {
                    HomeCategoryChips(
                        chipColor = homeState.onBackgroundColor,
                        chipKey = "movie-category",
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onAction = onAction,
                        navigateToMovie = navigateToMovie
                    )
                }

                Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    if (movies.itemCount > 0) {
                        val series = movies[0] as Movie
                        RecommendationSeries(
                            series = series,
                            onUpdateRecommendationSeriesHeight = onUpdateRecommendationSeriesHeight
                        ) {
                            Genre()
                            RecommendationButtons(navigateToVideo = navigateToVideo)
                        }
                    }
                }

                Group.HomeGroup.TODAY_TOP_10_MOVIES -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { index, movie ->
                        Top10MovieItem(index = index, movie = movie as Movie)
                    }
                }

                Group.HomeGroup.NOW_PLAYING_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.HomeGroup.TOP_RATED_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.HomeGroup.AIRING_TODAY_TV -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                Group.HomeGroup.ON_THE_AIR_TV -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                Group.HomeGroup.TOP_RATED_TV -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, tv ->
                        TvItem(tv = tv as Tv)
                    }
                }
            }
        }
    }
}

@Composable
private fun Top10MovieItem(index: Int, movie: Movie) {
    Row {
        Text(
            text = "${index + 1}",
            fontWeight = FontWeight.Bold,
            fontSize = 72.sp,
            fontStyle = FontStyle.Italic,
            color = White,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
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
}

@Composable
private fun TvItem(tv: Tv) {
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