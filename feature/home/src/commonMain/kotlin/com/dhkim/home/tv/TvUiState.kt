package com.dhkim.home.tv

import app.cash.paging.PagingData
import com.dhkim.common.Series
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

data class TvUiState(
    val displayState: TvDisplayState = TvDisplayState.Loading
)

sealed interface TvDisplayState {
    data object Loading : TvDisplayState
    data class Error(val errorCode: String, val message: String) : TvDisplayState
    data class Contents(val tvs: ImmutableList<SeriesItem>) : TvDisplayState
    data class CategoryContents(val tvs: StateFlow<PagingData<Series>>) : TvDisplayState
}