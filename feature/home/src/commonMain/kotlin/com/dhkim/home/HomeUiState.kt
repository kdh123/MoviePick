package com.dhkim.home

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val displayState: HomeDisplayState = HomeDisplayState.Loading
)

sealed interface HomeDisplayState {
    data object Loading : HomeDisplayState
    data class Error(val errorCode: String, val message: String) : HomeDisplayState
    data class Contents(val movies: StateFlow<PagingData<Movie>>) : HomeDisplayState
}
