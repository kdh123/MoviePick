package com.dhkim.home.movie

import androidx.compose.ui.graphics.Color
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList

data class MovieUiState(
    val backgroundColor: Color? = null,
    val onBackgroundColor: Color? = null,
    val displayState: MovieDisplayState = MovieDisplayState.Loading
)

sealed interface MovieDisplayState {
    data object Loading : MovieDisplayState
    data class Error(val errorCode: String, val message: String) : MovieDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : MovieDisplayState
}