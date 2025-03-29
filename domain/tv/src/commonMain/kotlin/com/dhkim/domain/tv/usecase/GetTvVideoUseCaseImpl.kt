package com.dhkim.domain.tv.usecase

import com.dhkim.common.Language
import com.dhkim.common.Video
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetTvVideoUseCaseImpl(
    private val tvRepository: TvRepository
) : GetTvVideoUseCase {

    override fun invoke(id: String, language: Language): Flow<Video?> {
        return flow {
            val tvVideos = tvRepository.getTvVideos(id, language).first()
            if (tvVideos.isNotEmpty()) {
                emit(tvVideos.shuffled().first())
            } else {
                emit(null)
            }
        }
    }
}