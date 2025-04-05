package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class GetTvReviewsUseCaseImpl(
    private val movieRepository: TvRepository
) : GetTvReviewsUseCase {

    override fun invoke(id: String): Flow<PagingData<Review>> {
        return movieRepository.getTvReviews(id)
    }
}