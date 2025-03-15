package com.dhkim.home

import com.dhkim.domain.movie.model.Movie

sealed interface HomeAction {

    data class GetTopRatedMovies(val movies: List<Movie>) : HomeAction
}