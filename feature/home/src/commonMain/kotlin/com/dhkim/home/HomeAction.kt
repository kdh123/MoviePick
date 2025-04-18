package com.dhkim.home

import com.dhkim.common.Series
import com.dhkim.common.SeriesType

sealed interface HomeAction {

    data class AddBookmark(val series: Series, val seriesType: SeriesType) : HomeAction
    data class DeleteBookmark(val series: Series, val seriesType: SeriesType) : HomeAction
}