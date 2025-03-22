package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class GetTopRatedTvsUseCase(
    private val tvRepository: TvRepository
) : GetTvsUseCase {

    override fun invoke(language: String): Flow<PagingData<Tv>> {
        return tvRepository.getTopRatedTvs(language)
    }
}