package com.dhkim.moviepick

import com.dhkim.common.Series

sealed class SeriesDetailItem(open val group: Group) {

    data class AppBar(
        override val group: Group = Group.AppBar
    ) : SeriesDetailItem(group)

    data class SeriesDetailPoster(
        override val group: Group = Group.SeriesPoster
    ) : SeriesDetailItem(group)

    data class Information(
        override val group: Group = Group.Information,
        val series: Series,
    ) : SeriesDetailItem(group)
}
