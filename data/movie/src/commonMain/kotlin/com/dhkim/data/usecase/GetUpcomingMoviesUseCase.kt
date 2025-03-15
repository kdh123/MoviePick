package com.dhkim.data.usecase

import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow

class GetUpcomingMoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(language: String, region: String): Flow<PagingData<Movie>> {
        return movieRepository.getUpcomingMovies(language, region)
    }
}