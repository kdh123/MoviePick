package com.dhkim.domain.movie.model

data class MovieVideo(
    val id: String,
    val videoUrl: String,
    val name: String,
    val type: MovieVideoType,
)

enum class MovieVideoType(val type: String) {
    Trailer("트레일러"),
    Teaser("티저")
}