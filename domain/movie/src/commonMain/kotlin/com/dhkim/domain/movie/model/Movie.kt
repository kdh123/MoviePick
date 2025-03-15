package com.dhkim.domain.movie.model

import com.dhkim.common.Series

data class Movie(
    override val id: String,
    val title: String,
    val adult: Boolean,
    val overview: String,
    val genre: List<String>,
    val releasedDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val imageUrl: String
) : Series