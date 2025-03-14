package com.dhkim.home

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.StateFlow

data class HomeMovieItem(
    val group: HomeMovieGroup,
    val movie: StateFlow<PagingData<Movie>>
)

enum class HomeMovieGroup(val title: String) {
    NOW_PLAYING(title = "Now Playing"),
    TOP_RATED(title = "TOP 10 시리즈"),
}
