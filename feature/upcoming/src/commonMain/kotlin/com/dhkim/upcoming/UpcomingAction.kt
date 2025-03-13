package com.dhkim.upcoming

import com.dhkim.core.movie.domain.model.Movie

sealed interface UpcomingAction {

    data class GetTopRatedMovies(val movies: List<Movie>) : UpcomingAction
}