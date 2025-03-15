package com.dhkim.home

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.StateFlow

data class HomeMovieItem(
    val group: HomeMovieGroup,
    val movie: StateFlow<PagingData<Movie>>
)

enum class HomeMovieGroup(val title: String) {
    NOW_PLAYING_TOP_10(title = "오늘 TOP 10 시리즈"),
    TOP_RATED(title = "평단의 극찬을 받은 명작"),
}
