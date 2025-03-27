package com.dhkim.domain.movie.model

import com.dhkim.common.Series
import kotlinx.datetime.Clock

data class Movie(
    override val id: String,
    override val imageUrl: String,
    override val key: String = "${Clock.System.now().toEpochMilliseconds()}",
    val title: String,
    val adult: Boolean,
    val overview: String,
    val genre: List<String>,
    val releasedDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val video: MovieVideo? = null,
) : Series {

    fun hasVideo() = video != null
}