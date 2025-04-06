package com.dhkim.moviepick

import com.dhkim.common.SeriesDetail
import com.dhkim.common.SeriesType

sealed class SeriesDetailItem(open val group: Group) {

    data class AppBar(
        override val group: Group = Group.AppBar
    ) : SeriesDetailItem(group)

    data class SeriesDetailPoster(
        override val group: Group = Group.SeriesPoster,
        val imageUrl: String
    ) : SeriesDetailItem(group)

    data class Information(
        override val group: Group = Group.Information,
        val seriesType: SeriesType,
        val series: SeriesDetail,
    ) : SeriesDetailItem(group)
}
