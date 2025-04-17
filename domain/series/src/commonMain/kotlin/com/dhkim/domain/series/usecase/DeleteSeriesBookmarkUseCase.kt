package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark

interface DeleteSeriesBookmarkUseCase {

    suspend operator fun invoke(seriesBookmark: SeriesBookmark)
}