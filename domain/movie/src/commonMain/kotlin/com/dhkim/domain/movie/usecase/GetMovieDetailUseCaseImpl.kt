package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMovieDetailUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMovieDetailUseCase {

    override fun invoke(id: String, language: Language): Flow<Movie> {
        return combine(
            movieRepository.getMovieDetail(id, language),
            movieRepository.getMovieVideos(id, language),
        ) { movie, videos ->
            movie.copy(
                video = videos.firstOrNull()
            )
        }
    }
}