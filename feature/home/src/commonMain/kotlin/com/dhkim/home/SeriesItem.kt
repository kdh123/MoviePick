package com.dhkim.home

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.filter
import app.cash.paging.map
import com.dhkim.common.Series
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

suspend fun Flow<PagingData<out Series>>.toContent(group: Group, scope: CoroutineScope): SeriesItem.Content {
    return SeriesItem.Content(
        group = group,
        series = map { pagingData ->
            val seenIds = mutableSetOf<String>()
            pagingData.filter { seenIds.add(it.id) }.map { it as Series }
        }
            .catch { }
            .cachedIn(scope)
            .stateIn(scope)
    )
}