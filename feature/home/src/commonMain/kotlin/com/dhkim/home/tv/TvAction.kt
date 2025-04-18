package com.dhkim.home.tv

import com.dhkim.common.Series
import com.dhkim.common.SeriesType

sealed interface TvAction {

    data class AddBookmark(val series: Series, val seriesType: SeriesType) : TvAction
    data class DeleteBookmark(val series: Series, val seriesType: SeriesType) : TvAction
}