package com.dhkim.tv.domain.model

import com.dhkim.common.Series

data class Tv(
    override val id: String,
    val title: String,
    val adult: Boolean,
    val country: String,
    val overview: String,
    val genre: List<String>,
    val firstAirDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val imageUrl: String
) : Series
