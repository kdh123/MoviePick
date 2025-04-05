package com.dhkim.core.testing.movie

import com.dhkim.common.Language
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetMovieDetailUseCase : GetMovieDetailUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override fun invoke(id: String, language: Language): Flow<Movie> {
        return flow {
            emit(movieRepository.getMovieDetail(id, language).first())
        }
    }
}