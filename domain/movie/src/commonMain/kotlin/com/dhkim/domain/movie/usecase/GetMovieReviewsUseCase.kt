package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Review
import kotlinx.coroutines.flow.Flow

interface GetMovieReviewsUseCase {

    operator fun invoke(id: String): Flow<PagingData<Review>>
}