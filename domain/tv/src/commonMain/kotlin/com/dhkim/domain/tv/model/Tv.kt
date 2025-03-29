package com.dhkim.domain.tv.model

import com.dhkim.common.Series
import com.dhkim.common.Video
import kotlinx.datetime.Clock

data class Tv(
    override val id: String,
    override val imageUrl: String,
    override val key: String = "${Clock.System.now().toEpochMilliseconds()}",
    override val title: String,
    override val adult: Boolean,
    override val overview: String,
    override val genre: List<String>,
    override val popularity: Double,
    override val voteAverage: Double,
    override val video: Video? = null,
    val country: String,
    val firstAirDate: String,
) : Series
