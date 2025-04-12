package com.dhkim.core.testing.movie

import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetUpcomingMoviesUseCase : GetUpcomingMoviesUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override fun invoke(language: Language, region: Region): Flow<List<Movie>> {
        return flow {
            if (currentStatus == MovieStatus.Success) {
                emit(movieRepository.getUpcomingMovies(page = 1, language, region).first())
            } else {
                throw Exception("upcoming movies error")
            }
        }
    }
}