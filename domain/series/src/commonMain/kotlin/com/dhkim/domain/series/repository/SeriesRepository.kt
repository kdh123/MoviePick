package com.dhkim.domain.series.repository

import com.dhkim.common.SeriesBookmark
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    fun getBookmarks(): Flow<List<SeriesBookmark>>
    suspend fun addBookmark(seriesBookmark: SeriesBookmark)
    suspend fun deleteBookmark(seriesBookmark: SeriesBookmark)
}