package com.dhkim.core.testing.movie

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.domain.movie.usecase.GetMovieReviewsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetMovieReviewsUseCase : GetMovieReviewsUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override fun invoke(id: String): Flow<PagingData<Review>> {
        return flow {
            emit(movieRepository.getMovieReviews(id).first())
        }
    }
}