package com.dhkim.core.testing.movie

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.core.testing.tv.FakeTvRepository
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take

class FakeGetTodayRecommendationTvUseCase : GetTvsUseCase {

    private var currentStatus = MovieStatus.Success

    private val tvRepository = FakeTvRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: Language): Flow<PagingData<Tv>> {
        return flow {
            if (currentStatus == MovieStatus.Success) {
                val movies = tvRepository.getAiringTodayTvs(language)
                    .take(1)
                    .first()
                emit(movies)
            } else {
                throw Exception("today recommendation tv error")
            }
        }
    }
}