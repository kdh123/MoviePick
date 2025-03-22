package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetTopRatedTvsUseCase : GetTvsUseCase {

    private var currentStatus = TvStatus.Success

    private val movieRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: Language): Flow<PagingData<Tv>> {
        return flow {
            if (currentStatus == TvStatus.Success) {
                emit(movieRepository.getTopRatedTvs(language).first())
            } else {
                throw Exception("top rated tvs error")
            }
        }
    }
}