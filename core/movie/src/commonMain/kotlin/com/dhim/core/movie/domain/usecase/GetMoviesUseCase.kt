package com.dhim.core.movie.domain.usecase

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetMoviesUseCase {

    operator fun invoke(): Flow<PagingData<Movie>>
}