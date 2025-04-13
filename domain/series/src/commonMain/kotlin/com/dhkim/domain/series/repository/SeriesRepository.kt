package com.dhkim.domain.series.repository

import com.dhkim.common.SeriesBookmark
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    fun getBookmarks(): Flow<List<SeriesBookmark>>
    fun addBookmark(seriesBookmark: SeriesBookmark)
    fun deleteBookmark(seriesBookmark: SeriesBookmark)
}