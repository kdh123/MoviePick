package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class GetOnTheAirTvsUseCase(
    private val tvRepository: TvRepository
) : GetTvsUseCase {

    override fun invoke(language: Language): Flow<PagingData<Tv>> {
        return tvRepository.getOnTheAirTvs(language)
    }
}