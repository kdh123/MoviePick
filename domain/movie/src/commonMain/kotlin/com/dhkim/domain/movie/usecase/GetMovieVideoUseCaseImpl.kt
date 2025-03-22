package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.domain.movie.model.MovieVideo
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetMovieVideoUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMovieVideoUseCase {

    override fun invoke(id: String, language: Language): Flow<MovieVideo?> {
        return flow {
            val movieVideos = movieRepository.getMovieVideos(id, language).first()
            if (movieVideos.isNotEmpty()) {
                emit(movieVideos.shuffled().first())
            } else {
                emit(null)
            }
        }
    }
}