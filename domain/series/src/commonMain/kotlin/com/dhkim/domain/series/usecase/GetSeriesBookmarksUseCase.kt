package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark
import kotlinx.coroutines.flow.Flow

interface GetSeriesBookmarksUseCase {

    operator fun invoke(): Flow<List<SeriesBookmark>>
}