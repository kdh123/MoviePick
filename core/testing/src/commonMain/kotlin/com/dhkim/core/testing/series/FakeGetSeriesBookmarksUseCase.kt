package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import kotlinx.coroutines.flow.Flow

class FakeGetSeriesBookmarksUseCase : GetSeriesBookmarksUseCase {

    private val seriesRepository = FakeSeriesRepository()

    override fun invoke(): Flow<List<SeriesBookmark>> {
        return seriesRepository.getBookmarks()
    }
}