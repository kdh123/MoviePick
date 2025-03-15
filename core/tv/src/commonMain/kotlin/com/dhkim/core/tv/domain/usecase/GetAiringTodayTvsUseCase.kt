package com.dhkim.core.tv.domain.usecase

import app.cash.paging.PagingData
import com.dhkim.core.tv.domain.model.Tv
import kotlinx.coroutines.flow.Flow

interface GetTvsUseCase {

    operator fun invoke(language: String): Flow<PagingData<Tv>>
}