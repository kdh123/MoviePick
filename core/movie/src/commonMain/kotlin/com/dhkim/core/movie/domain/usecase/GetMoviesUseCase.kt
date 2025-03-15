package com.dhkim.core.movie.domain.usecase

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetMoviesUseCase {

    operator fun invoke(language: String, region: String): Flow<PagingData<Movie>>
}