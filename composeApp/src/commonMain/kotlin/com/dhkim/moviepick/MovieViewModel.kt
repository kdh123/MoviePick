package com.dhkim.moviepick

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieViewModel : ViewModel(){

    val _uiState = MutableStateFlow(Movie())
    val uiState = _uiState.asStateFlow()

    var count = 0

    fun test() {
        _uiState.update { it.copy(name = "value ${count++}") }
    }
}

data class Movie(
    val name: String = "hello"
)