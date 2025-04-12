package com.dhkim.domain.tv.usecase

import com.dhkim.common.ImageType
import com.dhkim.common.Language
import com.dhkim.domain.tv.model.TvDetail
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTvDetailUseCaseImpl(
    private val tvRepository: TvRepository
) : GetTvDetailUseCase {

    override fun invoke(id: String, language: Language): Flow<TvDetail> {
        return combine(
            tvRepository.getTvDetail(id, language),
            tvRepository.getTvVideos(id, language),
            tvRepository.getTvReviews(id),
            tvRepository.getTvCastMembers(id, language),
            tvRepository.getTvImages(id)
        ) { tvDetail, videos, reviews, castMembers, images ->
            tvDetail.copy(
                images = images.filter { it.imageType == ImageType.Landscape }.map { it.imageUrl },
                videos = videos,
                review = reviews,
                actors = castMembers
            )
        }
    }
}