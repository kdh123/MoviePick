package com.dhkim.core.testing.movie

import com.dhkim.common.Language
import com.dhkim.common.Video
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

    override fun invoke(id: String, language: Language): Flow<Video?> {
        return flow {
            emit(movieRepository.getMovieVideos(id, language).first().shuffled().firstOrNull())
        }
    }
}