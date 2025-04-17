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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.ContentItem
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.SeriesList
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.diamondedge.logging.logging
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@ExperimentalSharedTransitionApi
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    bookmarks: ImmutableList<SeriesBookmark>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToMovie: () -> Unit,
    navigateToTv: () -> Unit,
    onBack: () -> Unit
) {
    val homeState = (uiState.displayState as? HomeDisplayState.Contents)?.movies?.let { homeMovieItems ->
        rememberHomeState(seriesItems = homeMovieItems, mainRecommendationSeriesGroup = Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE)
    } ?: rememberHomeState(seriesItems = persistentListOf(), mainRecommendationSeriesGroup = Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE)

    val backgroundGradientColors = listOf(
        homeState.backgroundColor,
        MaterialTheme.colorScheme.background
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
                    bookmarks = bookmarks,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAction = onAction,
                    navigateToSeriesDetail = navigateToSeriesDetail,
                    navigateToVideo = navigateToVideo,
                    navigateToMovie = navigateToMovie,
                    navigateToTv = navigateToTv
                )
            }

            is HomeDisplayState.Error -> {

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
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    navigateToMovie: () -> Unit,
    navigateToTv: () -> Unit
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
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "movie-category"),
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
                modifier = Modifier
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "tv-category"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .noRippleClick { navigateToTv() },
                borderColor = chipColor,
            ) {
                Text(
                    text = "TV 프로그램",
                    color = chipColor,
                    style = MoviePickTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun ContentsScreen(
    homeState: HomeState,
    homeSeriesItems: ImmutableList<SeriesItem>,
    bookmarks: ImmutableList<SeriesBookmark>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (HomeAction) -> Unit,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToMovie: () -> Unit,
    navigateToTv: () -> Unit,
    navigateToVideo: (String) -> Unit,
) {
    val onBackgroundColor by animateColorAsState(
        targetValue = if (homeState.showCategory) MaterialTheme.colorScheme.onBackground else homeState.onBackgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    Box {
        LazyColumn(
            state = homeState.listState,
        ) {
            items(items = homeSeriesItems, key = { item -> item.group }) { item ->
                when (item.group as Group.HomeGroup) {
                    Group.HomeGroup.APP_BAR -> {
                        AppBar()
                    }

                    Group.HomeGroup.CATEGORY -> {
                        HomeCategoryChips(
                            chipColor = homeState.onBackgroundColor,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onAction = onAction,
                            navigateToMovie = navigateToMovie,
                            navigateToTv = navigateToTv
                        )
                    }

                    Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        if (movies.itemCount > 0) {
                            val series = movies[0] as Movie
                            val isBookmarked = bookmarks.any { it.id == series.id }
                            RecommendationSeries(
                                series = series,
                                onClick = { navigateToSeriesDetail(SeriesType.MOVIE, series.id) },
                                onUpdateRecommendationSeriesHeight = homeState::updateHeight
                            ) {
                                Genre()
                                RecommendationButtons(
                                    isBookmarked = isBookmarked,
                                    onBookmarkClick = { onAction(HomeAction.AddBookmark(it)) },
                                    navigateToVideo = navigateToVideo
                                )
                            }
                        }
                    }

                    Group.HomeGroup.TODAY_TOP_10_MOVIES -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { index, movie ->
                            Top10MovieItem(
                                index = index,
                                movie = movie as Movie,
                                onClick = { navigateToSeriesDetail(SeriesType.MOVIE, movie.id) }
                            )
                        }
                    }
                    Group.HomeGroup.NOW_PLAYING_MOVIE,
                    Group.HomeGroup.TOP_RATED_MOVIE -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, movie ->
                            ContentItem(
                                series = movie as Movie,
                                onClick = {
                                    navigateToSeriesDetail(SeriesType.MOVIE, movie.id)
                                }
                            )
                        }
                    }
                    Group.HomeGroup.AIRING_TODAY_TV,
                    Group.HomeGroup.ON_THE_AIR_TV,
                    Group.HomeGroup.TOP_RATED_TV -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        SeriesList(title = (item.group as Group.HomeGroup).title, series = movies) { _, tv ->
                            ContentItem(
                                series = tv as Tv,
                                onClick = {
                                    logging().info { "tvId : ${tv.id}" }
                                    navigateToSeriesDetail(SeriesType.TV, tv.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (homeState.showTab) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = homeState.backgroundColor)
                ) {
                    HomeCategoryChips(
                        chipColor = onBackgroundColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onAction = {},
                        navigateToMovie = navigateToMovie,
                        navigateToTv = navigateToTv
                    )
                }
            }
        }
    }
}

@Composable
private fun Top10MovieItem(
    index: Int,
    movie: Movie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .noRippleClick(onClick)
    ) {
        Text(
            text = "${index + 1}",
            fontWeight = FontWeight.Bold,
            fontSize = 72.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground,
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
