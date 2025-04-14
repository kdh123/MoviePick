package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark

interface DeleteSeriesBookmarkUseCase {

    suspend fun invoke(seriesBookmark: SeriesBookmark)
}