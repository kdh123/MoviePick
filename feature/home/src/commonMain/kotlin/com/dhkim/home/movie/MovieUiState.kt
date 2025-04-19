package com.dhkim.home.movie

import androidx.compose.runtime.Stable
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList

@Stable
data class MovieUiState(
    val displayState: MovieDisplayState = MovieDisplayState.Loading
)

@Stable
sealed interface MovieDisplayState {
    data object Loading : MovieDisplayState
    data class Error(val code: Int, val message: String) : MovieDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : MovieDisplayState
}