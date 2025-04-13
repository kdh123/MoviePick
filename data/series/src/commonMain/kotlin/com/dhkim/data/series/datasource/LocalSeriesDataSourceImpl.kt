package com.dhkim.data.series.datasource

import com.dhkim.common.SeriesBookmark
import com.dhkim.data.series.toSeriesBookmark
import com.dhkim.data.series.toSeriesBookmarkEntity
import com.dhkim.database.SeriesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSeriesDataSourceImpl(
    private val db: SeriesDatabase
) : LocalSeriesDataSource {

    override fun getBookmarks(): Flow<List<SeriesBookmark>> {
        return db.seriesBookmarkDao().getBookmarks().map {
            it.map { bookmark ->
                bookmark.toSeriesBookmark()
            }
        }
    }

    override fun addBookmark(seriesBookmark: SeriesBookmark) {
        db.seriesBookmarkDao().insert(seriesBookmark.toSeriesBookmarkEntity())
    }

    override fun deleteBookmark(seriesBookmark: SeriesBookmark) {
        db.seriesBookmarkDao().delete(seriesBookmark.toSeriesBookmarkEntity())
    }
}