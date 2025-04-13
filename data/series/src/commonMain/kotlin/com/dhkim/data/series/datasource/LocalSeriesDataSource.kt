package com.dhkim.data.series.datasource

import com.dhkim.common.SeriesBookmark
import kotlinx.coroutines.flow.Flow

interface LocalSeriesDataSource {

    fun getBookmarks(): Flow<List<SeriesBookmark>>
    fun addBookmark(seriesBookmark: SeriesBookmark)
    fun deleteBookmark(seriesBookmark: SeriesBookmark)
}