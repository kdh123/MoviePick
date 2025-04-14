package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.repository.SeriesRepository

class DeleteSeriesBookmarkUseCaseImpl(
    private val seriesRepository: SeriesRepository
) : DeleteSeriesBookmarkUseCase {

    override suspend operator fun invoke(seriesBookmark: SeriesBookmark) {
        seriesRepository.deleteBookmark(seriesBookmark)
    }
}