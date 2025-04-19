package com.dhkim.home

import app.cash.paging.PagingData
import com.dhkim.common.Series
import kotlinx.coroutines.flow.StateFlow

sealed class SeriesItem(open val group: Group) {

    data class AppBar(
        override val group: Group
    ) : SeriesItem(group)

    data class Category(
        override val group: Group
    ) : SeriesItem(group)

    data class Content(
        override val group: Group,
        val series: StateFlow<PagingData<Series>>,
    ) : SeriesItem(group)
}