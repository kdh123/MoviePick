package com.dhkim.core.testing.movie

import com.dhkim.domain.movie.model.MovieVideo
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetMovieVideoUseCase : GetMovieVideoUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override fun invoke(id: String, language: String): Flow<MovieVideo?> {
        return flow {
            emit(movieRepository.getMovieVideos(id, language).first().shuffled().firstOrNull())
        }
    }
}