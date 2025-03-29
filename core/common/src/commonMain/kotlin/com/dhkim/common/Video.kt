package com.dhkim.common

data class Video(
    val id: String,
    val key: String,
    val videoUrl: String,
    val name: String,
    val type: VideoType,
)

enum class VideoType(val type: String) {
    Trailer("트레일러"),
    Teaser("티저")
}
