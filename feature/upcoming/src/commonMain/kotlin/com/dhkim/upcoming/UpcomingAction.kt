package com.dhkim.upcoming

import com.dhkim.domain.movie.model.Movie

sealed interface UpcomingAction {

    data class GetTopRatedMovies(val movies: List<Movie>) : UpcomingAction
}