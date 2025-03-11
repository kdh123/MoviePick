package com.dhkim.core.movie.data.usecase

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow

class GetTopRatedMoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(): Flow<PagingData<Movie>> {
        return movieRepository.getTopRatedMovies()
    }
}