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
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.common.Series
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val todayRecommendationMovie = mutableListOf<Series>().apply {
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

val topRatedMovies = mutableListOf<Series>().apply {
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

val nowPlayingMovies = mutableListOf<Series>().apply {
    repeat(50) {
        add(
            Movie(
                id = "nowPlayingId$it",
                title = "now playing title$it",
                adult = false,
                overview = "overview $it",
                genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl$it",
                releasedDate = "2025-02-05",
                voteAverage = 5.5 + it.toDouble(),
                popularity = 45.38 + it.toDouble(),
            )
        )
    }
}

val airingTodayTvs = mutableListOf<Series>().apply {
    repeat(50) {
        add(
            Tv(
                id = "airingTodayId$it",
                title = "airing today title$it",
                adult = false,
                country = Region.Korea.country,
                overview = "overview $it",
                genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl$it",
                firstAirDate = "2025-03-13",
                voteAverage = 5.5 + it.toDouble(),
                popularity = 45.38 + it.toDouble()
            )
        )
    }
}

val onTheAirTvs = mutableListOf<Series>().apply {
    repeat(50) {
        add(
            Tv(
                id = "onTheAirId$it",
                title = "on the air title$it",
                adult = false,
                country = Region.Korea.country,
                overview = "overview $it",
                genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl$it",
                firstAirDate = "2025-03-13",
                voteAverage = 5.5 + it.toDouble(),
                popularity = 45.38 + it.toDouble()
            )
        )
    }
}

val topRatedTvs = mutableListOf<Series>().apply {
    repeat(50) {
        add(
            Tv(
                id = "topRatedId$it",
                title = "top rated title$it",
                adult = false,
                country = Region.Korea.country,
                overview = "overview $it",
                genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl$it",
                firstAirDate = "2025-03-13",
                voteAverage = 5.5 + it.toDouble(),
                popularity = 45.38 + it.toDouble()
            )
        )
    }
}

val todayRecommendationMovieStateFlow = MutableStateFlow(PagingData.from(todayRecommendationMovie)).asStateFlow()
val todayRecommendationSeriesItem = SeriesItem.Content(Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE, todayRecommendationMovieStateFlow)

val todayTop10MovieStateFlow = MutableStateFlow(PagingData.from(nowPlayingMovies)).asStateFlow()
val todayTop10SeriesItem = SeriesItem.Content(Group.HomeGroup.TODAY_TOP_10_MOVIES, todayTop10MovieStateFlow)

val topRatedMoviesStateFlow = MutableStateFlow(PagingData.from(topRatedMovies)).asStateFlow()
val topRatedMoviesItem = SeriesItem.Content(Group.HomeGroup.TOP_RATED_MOVIE, topRatedMoviesStateFlow)

val nowPlayingMoviesStateFlow = MutableStateFlow(PagingData.from(nowPlayingMovies)).asStateFlow()
val nowPlayingMoviesItem = SeriesItem.Content(Group.HomeGroup.NOW_PLAYING_MOVIE, nowPlayingMoviesStateFlow)

val airingTodayTvsStateFlow = MutableStateFlow(PagingData.from(airingTodayTvs)).asStateFlow()
val airingTodayTvsItem = SeriesItem.Content(Group.HomeGroup.AIRING_TODAY_TV, airingTodayTvsStateFlow)

val onTheAirTvsStateFlow = MutableStateFlow(PagingData.from(onTheAirTvs)).asStateFlow()
val onTheAirTvsItem = SeriesItem.Content(Group.HomeGroup.ON_THE_AIR_TV, onTheAirTvsStateFlow)

val topRatedTvsStateFlow = MutableStateFlow(PagingData.from(topRatedTvs)).asStateFlow()
val topRatedTvsItem = SeriesItem.Content(Group.HomeGroup.ON_THE_AIR_TV, topRatedTvsStateFlow)

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalSharedTransitionApi
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    val series = persistentListOf(
        SeriesItem.AppBar(group = Group.HomeGroup.APP_BAR),
        SeriesItem.Category(group = Group.HomeGroup.CATEGORY),
        todayRecommendationSeriesItem,
        todayTop10SeriesItem,
        topRatedMoviesItem,
        nowPlayingMoviesItem,
        airingTodayTvsItem,
        onTheAirTvsItem,
        topRatedTvsItem
    )

    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = false, label = "") {
                    HomeScreen(
                        uiState = HomeUiState(displayState = HomeDisplayState.Contents(series)),
                        bookmarks = persistentListOf(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onAction = {},
                        navigateToSeriesDetail = { _, _ -> },
                        navigateToVideo = {},
                        navigateToMovie = {},
                        navigateToTv = {},
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
private fun HomeScreenPreview() {
    val series = persistentListOf(
        SeriesItem.AppBar(group = Group.HomeGroup.APP_BAR),
        SeriesItem.Category(group = Group.HomeGroup.CATEGORY),
        todayRecommendationSeriesItem,
        todayTop10SeriesItem,
        topRatedMoviesItem,
        nowPlayingMoviesItem,
        airingTodayTvsItem,
        onTheAirTvsItem,
        topRatedTvsItem
    )

    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = false, label = "") {
                    HomeScreen(
                        uiState = HomeUiState(displayState = HomeDisplayState.Contents(series)),
                        bookmarks = persistentListOf(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onAction = {},
                        navigateToSeriesDetail = { _, _ -> },
                        navigateToVideo = {},
                        navigateToMovie = {},
                        navigateToTv = {},
                        onBack = {}
                    )
                }
            }
        }
    }
}