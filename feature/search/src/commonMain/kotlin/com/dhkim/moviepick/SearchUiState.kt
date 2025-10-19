package com.dhkim.moviepick

import androidx.compose.runtime.Stable
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import kotlinx.collections.immutable.ImmutableList

@Stable
data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val contentState: SearchContentState = SearchContentState.Idle
)

sealed interface SearchContentState {

    data object Idle: SearchContentState
    data object Empty : SearchContentState
    data class Content(val movies: ImmutableList<Movie>, val tvs: ImmutableList<Tv>) : SearchContentState
    data class Error(val message: String) : SearchContentState
}
