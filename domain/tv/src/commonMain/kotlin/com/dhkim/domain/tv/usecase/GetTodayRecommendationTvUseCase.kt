package com.dhkim.domain.tv.usecase

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetTodayRecommendationTvUseCase(
    private val tvRepository: TvRepository,
    private val getTvVideoUseCase: GetTvVideoUseCase
) : GetTvsUseCase {

    override fun invoke(language: Language): Flow<PagingData<Tv>> {
        return flow {
            val index = (0 until 10).random()
            val airingTodayTvs = tvRepository.getAiringTodayTvs(language)
                .asSnapshot()
                .take(10)
            val movie = (airingTodayTvs)[index]
            val video = getTvVideoUseCase(movie.id, language).first()
            emit(PagingData.from(listOf(movie.copy(video = video))))
        }
    }
}