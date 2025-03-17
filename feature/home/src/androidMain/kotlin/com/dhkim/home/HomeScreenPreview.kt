package com.dhkim.home

import android.content.res.Configuration
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
            video = true
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
                video = true
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
                video = true
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
val todayRecommendationMovieItem = HomeItem.HomeMovieItem(HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE, todayRecommendationMovieStateFlow)

val topRatedMoviesStateFlow = MutableStateFlow(PagingData.from(topRatedMovies)).asStateFlow()
val topRatedMoviesItem = HomeItem.HomeMovieItem(HomeMovieGroup.TOP_RATED_MOVIE, topRatedMoviesStateFlow)

val nowPlayingMoviesStateFlow = MutableStateFlow(PagingData.from(nowPlayingMovies)).asStateFlow()
val nowPlayingMoviesItem = HomeItem.HomeMovieItem(HomeMovieGroup.NOW_PLAYING_MOVIE_TOP_10, nowPlayingMoviesStateFlow)

val airingTodayTvsStateFlow = MutableStateFlow(PagingData.from(airingTodayTvs)).asStateFlow()
val airingTodayTvsItem = HomeItem.HomeMovieItem(HomeMovieGroup.AIRING_TODAY_TV, airingTodayTvsStateFlow)

val onTheAirTvsStateFlow = MutableStateFlow(PagingData.from(onTheAirTvs)).asStateFlow()
val onTheAirTvsItem = HomeItem.HomeMovieItem(HomeMovieGroup.ON_THE_AIR_TV, onTheAirTvsStateFlow)

val topRatedTvsStateFlow = MutableStateFlow(PagingData.from(topRatedTvs)).asStateFlow()
val topRatedTvsItem = HomeItem.HomeMovieItem(HomeMovieGroup.ON_THE_AIR_TV, topRatedTvsStateFlow)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    val series = persistentListOf(
        HomeItem.AppBar(),
        HomeItem.Category(),
        todayRecommendationMovieItem,
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
            HomeScreen(
                uiState = HomeUiState(displayState = HomeDisplayState.Contents(series))
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() {
    val series = persistentListOf(
        todayRecommendationMovieItem,
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
            HomeScreen(
                uiState = HomeUiState(displayState = HomeDisplayState.Contents(series))
            )
        }
    }
}