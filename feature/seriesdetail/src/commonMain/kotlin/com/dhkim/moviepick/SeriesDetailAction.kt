package com.dhkim.moviepick

import com.dhkim.common.SeriesDetail
import com.dhkim.common.SeriesType

sealed interface SeriesDetailAction {

    data class AddBookmark(val series: SeriesDetail, val seriesType: SeriesType) : SeriesDetailAction
    data class DeleteBookmark(val series: SeriesDetail, val seriesType: SeriesType) : SeriesDetailAction
}