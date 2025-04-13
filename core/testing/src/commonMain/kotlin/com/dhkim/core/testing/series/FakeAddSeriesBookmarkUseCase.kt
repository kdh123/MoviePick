package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase

class FakeAddSeriesBookmarkUseCase : AddSeriesBookmarkUseCase {

    private val seriesRepository = FakeSeriesRepository()

    override fun invoke(seriesBookmark: SeriesBookmark) {
        seriesRepository.addBookmark(seriesBookmark)
    }
}