package com.dhkim.home.movie

import com.dhkim.common.Series
import com.dhkim.common.SeriesType

sealed interface MovieAction {

    data class AddBookmark(val series: Series, val seriesType: SeriesType) : MovieAction
    data class DeleteBookmark(val series: Series, val seriesType: SeriesType) : MovieAction
}