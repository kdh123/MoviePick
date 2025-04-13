package com.dhkim.bookmark

import androidx.compose.runtime.Stable
import com.dhkim.common.SeriesBookmark
import kotlinx.collections.immutable.ImmutableList

@Stable
data class BookmarkUiState(
    val displayState: BookmarkDisplayState = BookmarkDisplayState.Loading
)

@Stable
sealed interface BookmarkDisplayState {
    data object Loading : BookmarkDisplayState
    data class Contents(val series: ImmutableList<SeriesBookmark>) : BookmarkDisplayState
    data class Error(val errorCode: String, val message: String) : BookmarkDisplayState
}