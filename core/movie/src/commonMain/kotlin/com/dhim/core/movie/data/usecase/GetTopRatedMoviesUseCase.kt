package com.dhim.core.movie.data.usecase

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import com.dhim.core.movie.domain.repository.MovieRepository
import com.dhim.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow

class GetTopRatedMoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(): Flow<PagingData<Movie>> {
        return movieRepository.getTopRatedMovies()
    }
}