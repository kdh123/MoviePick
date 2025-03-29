package com.dhkim.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Series
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.Movie
import com.dhkim.home.movie.MovieDisplayState
import com.dhkim.home.movie.MovieScreen
import com.dhkim.home.movie.MovieUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val todayRecommendationMovie2 = mutableListOf<Series>().apply {
    add(
        Movie(
            id = "recommendationId",
            title = "recommendation title",
            adult = false,
            overview = "overview",
            genre = listOf(Genre.SCIENCE_FICTION.genre, Genre.ACTION.genre, Genre.DRAMA.genre),
            imageUrl = "imageUrl",
            releasedDate = "2025-03-13",
            voteAverage = 5.5,
            popularity = 45.38,
        )
    )
}

val moviesWithCategory = mutableListOf<Series>().apply {
    repeat(50) {
        add(
            Movie(
                id = "topRatedId$it",
                title = "top rated title$it",
                adult = false,
                overview = "overview $it",
                genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl$it",
                releasedDate = "2025-03-13",
                voteAverage = 5.5 + it.toDouble(),
                popularity = 45.38 + it.toDouble(),
            )
        )
    }
}

val todayRecommendationMovieStateFlow2 = MutableStateFlow(PagingData.from(todayRecommendationMovie2)).asStateFlow()
val todayRecommendationSeriesItem2 = SeriesItem.Content(Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE, todayRecommendationMovieStateFlow2)

val moviesWithCategoryStateFlow = MutableStateFlow(PagingData.from(moviesWithCategory)).asStateFlow()
val actionMoviesSeriesItem = SeriesItem.Content(Group.MovieGroup.ACTION_MOVIE, moviesWithCategoryStateFlow)
val animationSeriesItem = SeriesItem.Content(Group.MovieGroup.ANIMATION_MOVIE, moviesWithCategoryStateFlow)

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalSharedTransitionApi
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MovieScreenDarkPreview(
    @PreviewParameter(MovieUiStatePreviewProvider::class) uiState: MovieUiState
) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = false, label = "") {
                    MovieScreen(
                        uiState = uiState,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onAction = {},
                        navigateToVideo = {},
                        onBack = {}
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalSharedTransitionApi
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MovieScreenPreview(
    @PreviewParameter(MovieUiStatePreviewProvider::class) uiState: MovieUiState
) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = false, label = "") {
                    MovieScreen(
                        uiState = uiState,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onAction = {},
                        navigateToVideo = {},
                        onBack = {}
                    )
                }
            }
        }
    }
}

class MovieUiStatePreviewProvider : PreviewParameterProvider<MovieUiState> {

    private val series = persistentListOf(
        SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
        SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
        todayRecommendationSeriesItem2,
        actionMoviesSeriesItem,
        animationSeriesItem
    )

    override val values: Sequence<MovieUiState>
        get() = sequenceOf(
            MovieUiState(displayState = MovieDisplayState.Contents(series)),
            MovieUiState(displayState = MovieDisplayState.CategoryContents(moviesWithCategoryStateFlow)),
        )
}