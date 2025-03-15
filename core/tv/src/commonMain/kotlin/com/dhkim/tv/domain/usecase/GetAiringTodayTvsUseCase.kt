package com.dhkim.tv.domain.usecase

import app.cash.paging.PagingData
import com.dhkim.tv.domain.model.Tv
import kotlinx.coroutines.flow.Flow

interface GetTvsUseCase {

    operator fun invoke(language: String): Flow<PagingData<Tv>>
}