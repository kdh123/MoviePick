package com.dhkim.common

interface Series {

    val id: String
    val imageUrl: String
    val key: String
    val title: String
    val adult: Boolean
    val overview: String
    val genre: List<String>
    val popularity: Double
    val voteAverage: Double
    val video: Video?
    val isBookmarked: Boolean
}
