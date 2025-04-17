package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.data.series.datasource.LocalSeriesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalSeriesDataSource : LocalSeriesDataSource {

    private val bookmarks = mutableListOf<SeriesBookmark>().apply {
        repeat(50) {
            add(
                SeriesBookmark(
                    id = "nowPlayingId$it",
                    title = "title$it",
                    imageUrl = "imageUrl$it"
                )
            )
        }
    }

    private val bookmarkStateFlow = MutableStateFlow(bookmarks)

    override fun getBookmarks(): Flow<List<SeriesBookmark>> {
        return bookmarkStateFlow
    }

    override suspend fun addBookmark(seriesBookmark: SeriesBookmark) {
        bookmarks.add(seriesBookmark)
        bookmarkStateFlow.value = bookmarks
    }

    override suspend fun deleteBookmark(seriesBookmark: SeriesBookmark) {
        bookmarks.remove(seriesBookmark)
        bookmarkStateFlow.value = bookmarks
    }
}