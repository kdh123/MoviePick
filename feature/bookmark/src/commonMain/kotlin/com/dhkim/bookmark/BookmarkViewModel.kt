package com.dhkim.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BookmarkViewModel(
    private val getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
    private val deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase
) : ViewModel() {

    val uiState : StateFlow<BookmarkUiState> = getSeriesBookmarksUseCase()
        .map { BookmarkUiState(displayState = BookmarkDisplayState.Contents(series = it.toImmutableList())) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BookmarkUiState(displayState = BookmarkDisplayState.Loading)
        )
}