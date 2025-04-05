package com.dhkim.domain.tv.usecase

import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTvDetailUseCaseImpl(
    private val tvRepository: TvRepository
) : GetTvDetailUseCase {

    override fun invoke(id: String, language: Language): Flow<Tv> {
        return combine(
            tvRepository.getTvDetail(id, language),
            tvRepository.getTvVideos(id, language),
        ) { movie, videos ->
            movie.copy(
                video = videos.firstOrNull()
            )
        }
    }
}