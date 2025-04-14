package com.dhkim.data.series.repository

import com.dhkim.common.SeriesBookmark
import com.dhkim.data.series.datasource.LocalSeriesDataSource
import com.dhkim.domain.series.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow

class SeriesRepositoryImpl(
    private val localSeriesDataSource: LocalSeriesDataSource
) : SeriesRepository {

    override fun getBookmarks(): Flow<List<SeriesBookmark>> {
        return localSeriesDataSource.getBookmarks()
    }

    override suspend fun addBookmark(seriesBookmark: SeriesBookmark) {
        localSeriesDataSource.addBookmark(seriesBookmark)
    }

    override suspend fun deleteBookmark(seriesBookmark: SeriesBookmark) {
        localSeriesDataSource.deleteBookmark(seriesBookmark)
    }
}