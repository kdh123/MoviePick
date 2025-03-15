package com.dhkim.tv.data.usecase

import app.cash.paging.PagingData
import com.dhkim.tv.domain.model.Tv
import com.dhkim.tv.domain.repository.TvRepository
import com.dhkim.tv.domain.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow

class GetAiringTodayTvsUseCase(
    private val tvRepository: TvRepository
) : GetTvsUseCase {

    override fun invoke(language: String): Flow<PagingData<Tv>> {
        return tvRepository.getAiringTodayTvs(language)
    }
}