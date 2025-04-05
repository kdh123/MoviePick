package com.dhkim.core.testing.tv

import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetTvDetailUseCase : GetTvDetailUseCase {

    private var currentStatus = TvStatus.Success

    private val tvRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override fun invoke(id: String, language: Language): Flow<Tv> {
        return flow {
            emit(tvRepository.getTvDetail(id, language).first())
        }
    }
}