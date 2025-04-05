package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetTvReviewsUseCase : GetTvReviewsUseCase {

    private var currentStatus = TvStatus.Success

    private val tvRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override fun invoke(id: String): Flow<PagingData<Review>> {
        return flow {
            emit(tvRepository.getTvReviews(id).first())
        }
    }
}