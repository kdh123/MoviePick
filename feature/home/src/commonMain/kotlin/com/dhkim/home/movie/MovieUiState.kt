package com.dhkim.home.movie

import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList

data class MovieUiState(
    val displayState: MovieDisplayState = MovieDisplayState.Loading
)

sealed interface MovieDisplayState {
    data object Loading : MovieDisplayState
    data class Error(val errorCode: String, val message: String) : MovieDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : MovieDisplayState
}