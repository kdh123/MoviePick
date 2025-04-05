package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieReviewsUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMovieReviewsUseCase {

    override fun invoke(id: String): Flow<PagingData<Review>> {
        return movieRepository.getMovieReviews(id)
    }
}