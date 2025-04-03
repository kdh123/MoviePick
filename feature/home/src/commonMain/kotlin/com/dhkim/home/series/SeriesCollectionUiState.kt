package com.dhkim.home.series

import androidx.compose.runtime.Stable
import app.cash.paging.PagingData
import com.dhkim.common.Series
import kotlinx.coroutines.flow.StateFlow

@Stable
data class SeriesCollectionUiState(
    val categoryName: String = "",
    val displayState: SeriesCollectionDisplayState = SeriesCollectionDisplayState.Loading
)

@Stable
sealed interface SeriesCollectionDisplayState {
    data object Loading : SeriesCollectionDisplayState
    data class Contents(
        val series: StateFlow<PagingData<Series>>
    ) : SeriesCollectionDisplayState

    data class Error(
        val message: String
    ) : SeriesCollectionDisplayState
}
