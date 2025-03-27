package com.dhkim.home.movie

import app.cash.paging.PagingData
import com.dhkim.common.Series
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

data class MovieUiState(
    val displayState: MovieDisplayState = MovieDisplayState.Loading
)

sealed interface MovieDisplayState {
    data object Loading : MovieDisplayState
    data class Error(val errorCode: String, val message: String) : MovieDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : MovieDisplayState
    data class CategoryContents(val movies: StateFlow<PagingData<Series>>) : MovieDisplayState
}