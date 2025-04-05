package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Review
import kotlinx.coroutines.flow.Flow

interface GetTvReviewsUseCase {

    operator fun invoke(id: String): Flow<PagingData<Review>>
}