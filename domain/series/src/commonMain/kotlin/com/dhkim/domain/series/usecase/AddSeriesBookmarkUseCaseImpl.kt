package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.repository.SeriesRepository

class AddSeriesBookmarkUseCaseImpl(
    private val seriesRepository: SeriesRepository
) : AddSeriesBookmarkUseCase {

    override suspend operator fun invoke(seriesBookmark: SeriesBookmark) {
        seriesRepository.addBookmark(seriesBookmark)
    }
}