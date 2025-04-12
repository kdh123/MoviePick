package com.dhkim.domain.movie.usecase

import com.dhkim.common.ImageType
import com.dhkim.common.Language
import com.dhkim.domain.movie.model.MovieDetail
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMovieDetailUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMovieDetailUseCase {

    override fun invoke(id: String, language: Language): Flow<MovieDetail> {
        return combine(
            movieRepository.getMovieDetail(id, language),
            movieRepository.getMovieVideos(id, language),
            movieRepository.getMovieReviews(id),
            movieRepository.getMovieActors(id, language),
            movieRepository.getMovieImages(id)
        ) { movieDetail, videos, reviews, actors, images ->

            movieDetail.copy(
                images = images.filter { it.imageType == ImageType.Landscape }.map { it.imageUrl },
                videos = videos,
                review = reviews,
                actors = actors
            )
        }
    }
}