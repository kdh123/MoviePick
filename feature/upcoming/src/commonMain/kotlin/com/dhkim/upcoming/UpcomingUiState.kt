package com.dhkim.upcoming

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.StateFlow

data class UpcomingUiState(
    val displayState: UpcomingDisplayState = UpcomingDisplayState.Loading
)

sealed interface UpcomingDisplayState {
    data object Loading : UpcomingDisplayState
    data class Error(val errorCode: String, val message: String) : UpcomingDisplayState
    data class Contents(val movies: StateFlow<PagingData<Movie>>) : UpcomingDisplayState
}
