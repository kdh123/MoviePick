package com.dhkim.home

import kotlinx.collections.immutable.ImmutableList

data class HomeUiState(
    val displayState: HomeDisplayState = HomeDisplayState.Loading
)

sealed interface HomeDisplayState {
    data object Loading : HomeDisplayState
    data class Error(val errorCode: String, val message: String) : HomeDisplayState
    data class Contents(val movies: ImmutableList<HomeItem>) : HomeDisplayState
}
