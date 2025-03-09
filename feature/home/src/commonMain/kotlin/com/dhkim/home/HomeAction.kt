package com.dhkim.home

import com.dhim.core.movie.domain.model.Movie

sealed interface HomeAction {

    data class GetTopRatedMovies(val movies: List<Movie>) : HomeAction
}