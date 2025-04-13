package com.dhkim.moviepick

import androidx.compose.runtime.Stable
import com.dhkim.common.SeriesType
import kotlinx.collections.immutable.ImmutableList

@Stable
data class SeriesDetailUiState(
    val seriesType: SeriesType,
    val displayState: SeriesDetailDisplayState = SeriesDetailDisplayState.Loading
)

@Stable
sealed interface SeriesDetailDisplayState {
    data object Loading : SeriesDetailDisplayState
    data class Error(val errorCode: String, val message: String) : SeriesDetailDisplayState
    data class Contents(val isUpcoming: Boolean, val series: ImmutableList<SeriesDetailItem>) : SeriesDetailDisplayState
}
