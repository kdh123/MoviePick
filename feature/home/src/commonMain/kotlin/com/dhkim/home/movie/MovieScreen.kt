package com.dhkim.home.movie

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.CircleCloseButton
import com.dhkim.core.ui.ContentItem
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.SeriesList
import com.dhkim.domain.movie.model.Movie
import com.dhkim.home.Category
import com.dhkim.home.CategoryModal
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
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onBack: () -> Unit
) {
    val homeState = (uiState.displayState as? MovieDisplayState.Contents)?.movies?.let { homeMovieItems ->
        rememberHomeState(seriesItems = homeMovieItems, mainRecommendationSeriesGroup = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE)
    } ?: rememberHomeState(seriesItems = persistentListOf(), mainRecommendationSeriesGroup = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE)

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
            MovieDisplayState.Loading -> {

            }

            is MovieDisplayState.Contents -> {
                val movies = uiState.displayState.movies
                ContentsScreen(
                    homeState = homeState,
                    movieSeriesItems = movies,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAction = onAction,
                    navigateToSeriesDetail = navigateToSeriesDetail,
                    navigateToVideo = navigateToVideo,
                    navigateToSeriesCollection = navigateToSeriesCollection,
                    onBack = onBack
                )
            }

            is MovieDisplayState.Error -> {

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
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onAction: (MovieAction) -> Unit,
    onBack: () -> Unit
) {
    val onBackgroundColor by animateColorAsState(
        targetValue = if (homeState.showCategory) MaterialTheme.colorScheme.onBackground else homeState.onBackgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    val selectedChipTextColor by animateColorAsState(
        targetValue = if (homeState.showCategory) MaterialTheme.colorScheme.background else homeState.backgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    Box {
        LazyColumn(state = homeState.listState) {
            items(items = movieSeriesItems, key = { item -> item.group }) { item ->
                when (item.group as Group.MovieGroup) {
                    Group.MovieGroup.APP_BAR -> {
                        AppBar(onBackGroundColor = homeState.onBackgroundColor)
                    }

                    Group.MovieGroup.CATEGORY -> {
                        MovieCategoryChips(
                            chipColor = homeState.onBackgroundColor,
                            selectedChipTextColor = selectedChipTextColor,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onCategoryClick = { homeState.showCategoryModal = !homeState.showCategoryModal },
                            onBack = onBack
                        )
                    }

                    Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        if (movies.itemCount > 0) {
                            val series = movies[0] as Movie
                            RecommendationSeries(
                                series = series,
                                onClick = { navigateToSeriesDetail(SeriesType.MOVIE, series.id) },
                                onUpdateRecommendationSeriesHeight = homeState::updateHeight
                            ) {
                                Genre()
                                RecommendationButtons(navigateToVideo = navigateToVideo)
                            }
                        }
                    }

                    Group.MovieGroup.ACTION_MOVIE,
                    Group.MovieGroup.MUSIC_MOVIE,
                    Group.MovieGroup.DRAMA_MOVIE,
                    Group.MovieGroup.THRILLER_MOVIE,
                    Group.MovieGroup.ADVENTURE_MOVIE,
                    Group.MovieGroup.ANIMATION_MOVIE -> {
                        val movies = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        SeriesList(title = (item.group as Group.MovieGroup).title, series = movies) { _, movie ->
                            ContentItem(
                                series = movie as Movie,
                                onClick = { navigateToSeriesDetail(SeriesType.MOVIE, movie.id) }
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
                    MovieCategoryChips(
                        chipColor = onBackgroundColor,
                        selectedChipTextColor = selectedChipTextColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onCategoryClick = { homeState.showCategoryModal = !homeState.showCategoryModal },
                        onBack = onBack
                    )
                }
            }
        }

        if (homeState.showCategoryModal) {
            val categories = mutableListOf<Category>().apply {
                Category.Region.entries.forEach { add(it) }
                Category.MovieGenre.entries.forEach { add(it) }
            }
            CategoryModal(
                categories = categories,
                onCategoryClick = {
                    when (it) {
                        is Category.Region -> navigateToSeriesCollection(SeriesType.MOVIE, null, it.code)
                        is Category.MovieGenre -> navigateToSeriesCollection(SeriesType.MOVIE, it.id, null)
                        else -> {}
                    }
                    homeState.showCategoryModal = false
                },
                onClose = { homeState.showCategoryModal = false }
            )
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
    chipColor: Color,
    selectedChipTextColor: Color,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCategoryClick: () -> Unit,
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
            CircleCloseButton(color = chipColor, onClick = onBack)

            Chip(
                modifier = Modifier
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "movie-category"),
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
                modifier = Modifier
                    .clickable(onClick = onCategoryClick),
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