package com.dhkim.home.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.handle
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toMovieItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MovieViewModel(
    private val getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState = _uiState
        .onStart { init() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MovieUiState()
        )

    private fun init() {
        viewModelScope.handle(
            block = {
                val shouldShowMovieGenres = listOf(
                    Genre.ACTION,
                    Genre.ROMANCE,
                    Genre.COMEDY,
                    Genre.THRILLER,
                    Genre.ADVENTURE,
                    Genre.ANIMATION
                ).map { it.genre }
                val language = Language.Korea.code
                val region = Region.Korea
                val jobs = mutableListOf<Deferred<SeriesItem.MovieSeriesItem>>()
                val genres = Genre.entries.filter { shouldShowMovieGenres.contains(it.genre) }

                for (genre in genres) {
                    val movieSeriesItem = async {
                        getMovieWithCategoryUseCase(language, genre, region)
                            .toMovieItem(group = Group.MovieGroup.valueOf(genre.genre), viewModelScope)
                    }
                    jobs.add(movieSeriesItem)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
                    SeriesItem.Category(group = Group.MovieGroup.CATEGORY)
                ) + jobs.awaitAll()
                _uiState.update { MovieUiState(MovieDisplayState.Contents(series.toImmutableList())) }
            },
            error = {

            }
        )
    }
}