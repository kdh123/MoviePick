package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark

interface DeleteSeriesBookmarkUseCase {

    operator fun invoke(seriesBookmark: SeriesBookmark)
}