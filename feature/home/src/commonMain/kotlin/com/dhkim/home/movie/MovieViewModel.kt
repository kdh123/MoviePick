package com.dhkim.home.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toContent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

@ExperimentalCoroutinesApi
class MovieViewModel(
    private val getMainRecommendationMoviesUseCase: GetMoviesUseCase,
    private val getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase
) : ViewModel() {

    private val shouldShowMovieGenres = listOf(
        Genre.ACTION,
        Genre.MUSIC,
        Genre.DRAMA,
        Genre.THRILLER,
        Genre.ADVENTURE,
        Genre.ANIMATION
    ).map { it.genre }

    private val appBarItems = listOf(
        SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
        SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
    ).toImmutableList()

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.onStart {
        init()
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = MovieUiState(displayState = MovieDisplayState.Contents(appBarItems))
    )

    private fun init() {
        viewModelScope.handle(
            block = {
                val language = Language.Korea
                val region = Region.Korea
                val jobs = mutableListOf<Deferred<SeriesItem.Content>>()
                val genres = Genre.entries.filter { shouldShowMovieGenres.contains(it.genre) }
                val mainRecommendationMovie = async {
                    getMainRecommendationMoviesUseCase(language, region)
                        .toContent(group = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE, scope = viewModelScope)
                }
                jobs.add(mainRecommendationMovie)
                for (genre in genres) {
                    val movieSeriesItem = async {
                        getMovieWithCategoryUseCase(language, genre, region)
                            .toContent(group = Group.MovieGroup.entries.first { it.genre == genre }, scope = viewModelScope)
                    }
                    jobs.add(movieSeriesItem)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
                    SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
                ) + jobs.awaitAll()

                _uiState.update { MovieUiState(displayState = MovieDisplayState.Contents(series.toImmutableList())) }
            },
            error = {
            }
        )
    }

    fun onAction(action: MovieAction) {

    }
}