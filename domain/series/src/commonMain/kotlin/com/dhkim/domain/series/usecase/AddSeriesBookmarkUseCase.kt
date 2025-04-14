package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark

interface AddSeriesBookmarkUseCase {

    suspend operator fun invoke(seriesBookmark: SeriesBookmark)
}