package com.dhkim.core.testing.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.data.series.datasource.LocalSeriesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalSeriesDataSource : LocalSeriesDataSource {

    private val bookmarks = mutableListOf<SeriesBookmark>().apply {
        repeat(10) {
            add(
                SeriesBookmark(
                    id = "id$it",
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

    override fun addBookmark(seriesBookmark: SeriesBookmark) {
        bookmarks.add(seriesBookmark)
        bookmarkStateFlow.value = bookmarks
    }

    override fun deleteBookmark(seriesBookmark: SeriesBookmark) {
        bookmarks.remove(seriesBookmark)
        bookmarkStateFlow.value = bookmarks
    }
}