package com.dhkim.domain.tv.model

import com.dhkim.common.Series
import kotlinx.datetime.Clock

data class Tv(
    override val id: String,
    override val imageUrl: String,
    override val key: String = "${Clock.System.now().toEpochMilliseconds()}",
    val title: String,
    val adult: Boolean,
    val country: String,
    val overview: String,
    val genre: List<String>,
    val firstAirDate: String,
    val popularity: Double,
    val voteAverage: Double,
) : Series
