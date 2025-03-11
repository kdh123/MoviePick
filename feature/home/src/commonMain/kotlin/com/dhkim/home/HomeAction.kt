package com.dhkim.home

import com.dhkim.core.movie.domain.model.Movie

sealed interface HomeAction {

    data class GetTopRatedMovies(val movies: List<Movie>) : HomeAction
}