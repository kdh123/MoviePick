package com.dhkim.domain.movie.model

import com.dhkim.common.Series
import com.dhkim.common.Video
import kotlinx.datetime.Clock

data class Movie(
    override val id: String,
    override val imageUrl: String?,
    override val key: String = "${Clock.System.now().toEpochMilliseconds()}",
    override val title: String,
    override val adult: Boolean,
    override val overview: String,
    override val genre: List<String>,
    override val popularity: Double,
    override val voteAverage: Double,
    override val video: Video? = null,
    override val isBookmarked: Boolean = false,
    val releasedDate: String
) : Series {

    fun hasVideo() = video != null
}