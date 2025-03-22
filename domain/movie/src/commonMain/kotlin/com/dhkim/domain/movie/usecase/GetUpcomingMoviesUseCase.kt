package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetUpcomingMoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(language: Language, region: Region): Flow<PagingData<Movie>> {
        return movieRepository.getUpcomingMovies(language, region)
    }
}