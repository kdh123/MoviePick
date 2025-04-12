package com.dhkim.upcoming

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
data class UpcomingUiState(
    val displayState: UpcomingDisplayState = UpcomingDisplayState.Loading
)

sealed interface UpcomingDisplayState {
    data object Loading : UpcomingDisplayState
    data class Error(val errorCode: String, val message: String) : UpcomingDisplayState
    data class Contents(val series: ImmutableList<FeaturedSeries>) : UpcomingDisplayState
}
