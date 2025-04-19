package com.dhkim.home

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
data class HomeUiState(
    val displayState: HomeDisplayState = HomeDisplayState.Loading
)

@Stable
sealed interface HomeDisplayState {
    data object Loading : HomeDisplayState
    data class Error(val code: Int, val message: String) : HomeDisplayState
    data class Contents(val series: ImmutableList<SeriesItem>) : HomeDisplayState
}
