package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.tv.domain.model.Tv
import com.dhkim.tv.domain.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetTopRatedTvsUseCase : GetTvsUseCase {

    private var currentStatus = TvStatus.Success

    private val movieRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: String): Flow<PagingData<Tv>> {
        return flow {
            if (currentStatus == TvStatus.Success) {
                emit(movieRepository.getTopRatedTvs(language).first())
            } else {
                throw Exception("top rated tvs error")
            }
        }
    }
}