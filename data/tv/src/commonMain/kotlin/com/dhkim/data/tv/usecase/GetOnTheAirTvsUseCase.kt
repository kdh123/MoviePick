package com.dhkim.data.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow

class GetOnTheAirTvsUseCase(
    private val tvRepository: TvRepository
) : GetTvsUseCase {

    override fun invoke(language: String): Flow<PagingData<Tv>> {
        return tvRepository.getOnTheAirTvs(language)
    }
}