package com.dhkim.upcoming

import com.dhkim.common.Series

data class FeaturedSeries(
    val series: Series,
    val group: FeaturedSeriesGroup
)

enum class FeaturedSeriesGroup {
    Upcoming,
    TopRated
}