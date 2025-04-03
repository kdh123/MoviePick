package com.dhkim.home.movie

import androidx.compose.runtime.Stable
import app.cash.paging.PagingData
import com.dhkim.common.Series
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

@Stable
data class MovieUiState(
    val displayState: MovieDisplayState = MovieDisplayState.Loading
)

@Stable
sealed interface MovieDisplayState {
    data object Loading : MovieDisplayState
    data class Error(val errorCode: String, val message: String) : MovieDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : MovieDisplayState
}