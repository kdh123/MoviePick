package com.dhkim.home.movie

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.Black70
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.CircleCloseButton
import com.dhkim.core.ui.MovieItem
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.SeriesList
import com.dhkim.domain.movie.model.Movie
import com.dhkim.home.Genre
import com.dhkim.home.Group
import com.dhkim.home.HomeState
import com.dhkim.home.RecommendationButtons
import com.dhkim.home.SeriesItem
import com.dhkim.home.rememberHomeState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@ExperimentalSharedTransitionApi
@Composable
fun MovieScreen(
    uiState: MovieUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onAction: (MovieAction) -> Unit,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    val homeState = (uiState.displayState as? MovieDisplayState.Contents)?.movies?.let { homeMovieItems ->
        rememberHomeState(seriesItems = homeMovieItems, mainRecommendationMovieGroup = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE)
    } ?: rememberHomeState(seriesItems = persistentListOf(), mainRecommendationMovieGroup = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE)

    val backgroundGradientColors = listOf(
        homeState.backgroundColor,
        Black50
    )

    val onBackgroundColor by animateColorAsState(
        targetValue = if (homeState.showCategory) White else homeState.onBackgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    val selectedChipTextColor by animateColorAsState(
        targetValue = if (homeState.showCategory) homeState.onBackgroundColor else White,
        animationSpec = tween(500),
        label = ""
    )

    Box(
        modifier = Modifier
            .background(Brush.verticalGradient(backgroundGradientColors))
            .fillMaxSize()
    ) {
        when (uiState.displayState) {
            MovieDisplayState.Loading -> {

            }

            is MovieDisplayState.Contents -> {
                val movies = uiState.displayState.movies
                ContentsScreen(
                    homeState = homeState,
                    movieSeriesItems = movies,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedVisibilityScope,
                    navigateToVideo = navigateToVideo,
                    onBack = onBack
                )
            }

            is MovieDisplayState.Error -> {

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
                    MovieCategoryChips(
                        chipKey = "movie-category",
                        chipColor = onBackgroundColor,
                        selectedChipTextColor = selectedChipTextColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onBack = onBack
                    )
                }
            }
        }
    }
}

@ExperimentalSharedTransitionApi
@Composable
fun ContentsScreen(
    homeState: HomeState,
    movieSeriesItems: ImmutableList<SeriesItem>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        state = homeState.listState,
    ) {
        items(items = movieSeriesItems, key = { item -> item.group }) { item ->
            when (item.group as Group.MovieGroup) {
                Group.MovieGroup.APP_BAR -> {
                    AppBar(onBackGroundColor = homeState.onBackgroundColor)
                }

                Group.MovieGroup.CATEGORY -> {
                    MovieCategoryChips(
                        chipKey = "movie-category",
                        chipColor = homeState.onBackgroundColor,
                        selectedChipTextColor = homeState.backgroundColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedContentScope,
                        onBack = onBack
                    )
                }

                Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    if (movies.itemCount > 0) {
                        val series = movies[0] as Movie
                        RecommendationSeries(
                            series = series,
                            onUpdateRecommendationSeriesHeight = homeState::updateHeight
                        ) {
                            Genre()
                            RecommendationButtons(navigateToVideo = navigateToVideo)
                        }
                    }
                }

                Group.MovieGroup.ACTION_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.MovieGroup.ROMANCE_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.MovieGroup.COMEDY_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.MovieGroup.THRILLER_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.MovieGroup.ADVENTURE_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                Group.MovieGroup.ANIMATION_MOVIE -> {
                    val movies = (item as SeriesItem.MovieSeriesItem).series.collectAsLazyPagingItems()
                    SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBar(onBackGroundColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = "영화",
            style = MoviePickTheme.typography.headlineSmallBold,
            color = onBackGroundColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun MovieCategoryChips(
    chipKey: String,
    chipColor: Color,
    selectedChipTextColor: Color,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    with(sharedTransitionScope) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            CircleCloseButton(
                color = chipColor,
                onClick = onBack
            )

            Chip(
                modifier = Modifier
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = chipKey),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                backgroundColor = chipColor,
                borderColor = chipColor,
            ) {
                Text(
                    text = "영화",
                    color = selectedChipTextColor,
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