package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase

class FakeDeleteSeriesBookmarkUseCase : DeleteSeriesBookmarkUseCase {

    private val seriesRepository = FakeSeriesRepository()

    override fun invoke(seriesBookmark: SeriesBookmark) {
        seriesRepository.deleteBookmark(seriesBookmark)
    }
}