package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetOnTheAirTvsUseCase : GetTvsUseCase {

    private var currentStatus = TvStatus.Success

    private val movieRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: String): Flow<PagingData<Tv>> {
        return flow {
            if (currentStatus == TvStatus.Success) {
                emit(movieRepository.getOnTheAirTvs(language).first())
            } else {
                throw Exception("on the air tvs error")
            }
        }
    }
}