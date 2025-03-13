package com.dhkim.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UpcomingViewModel : ViewModel() {

    val uiState = MutableStateFlow(UpcomingUiState())
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UpcomingUiState()
        )
}