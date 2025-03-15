package com.dhkim.core.tv.data.usecase

import app.cash.paging.PagingData
import com.dhkim.core.tv.domain.model.Tv
import com.dhkim.core.tv.domain.repository.TvRepository
import com.dhkim.core.tv.domain.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow

class GetAiringTodayTvsUseCase(
    private val tvRepository: TvRepository
) : GetTvsUseCase {

    override fun invoke(language: String): Flow<PagingData<Tv>> {
        return tvRepository.getAiringTodayTvs(language)
    }
}