package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow

class FakeSeriesRepository : SeriesRepository {

    private val localSeriesDataSource = FakeLocalSeriesDataSource()

    override fun getBookmarks(): Flow<List<SeriesBookmark>> {
        return localSeriesDataSource.getBookmarks()
    }

    override fun addBookmark(seriesBookmark: SeriesBookmark) {
        localSeriesDataSource.addBookmark(seriesBookmark)
    }

    override fun deleteBookmark(seriesBookmark: SeriesBookmark) {
        localSeriesDataSource.deleteBookmark(seriesBookmark)
    }
}