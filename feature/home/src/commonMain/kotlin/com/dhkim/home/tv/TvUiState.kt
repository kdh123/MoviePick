package com.dhkim.home.tv

import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList

data class TvUiState(
    val displayState: TvDisplayState = TvDisplayState.Loading
)

sealed interface TvDisplayState {
    data object Loading : TvDisplayState
    data class Error(val errorCode: String, val message: String) : TvDisplayState
    data class Contents(val tvs: ImmutableList<SeriesItem>) : TvDisplayState
}