package com.dhkim.home

import app.cash.paging.PagingData
import com.dhkim.common.Series
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val displayState: HomeDisplayState = HomeDisplayState.Loading
)

sealed interface HomeDisplayState {
    data object Loading : HomeDisplayState
    data class Error(val errorCode: String, val message: String) : HomeDisplayState
    data class Contents(val movies: ImmutableList<SeriesItem>) : HomeDisplayState
    data class CategoryContents(val movies: StateFlow<PagingData<Series>>) : HomeDisplayState
}
