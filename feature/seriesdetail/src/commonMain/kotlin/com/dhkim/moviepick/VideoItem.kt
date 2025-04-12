package com.dhkim.moviepick

import androidx.compose.runtime.Stable
import com.dhkim.common.Video
import com.dhkim.common.VideoType

@Stable
data class VideoItem(
    val id: String,
    val type: VideoType,
    val key: String,
    val videoUrl: String,
    val thumbnail: String,
)

fun Video.toVideoItem(thumbnail: String): VideoItem {
    return VideoItem(
        id = id,
        type = type,
        key = key,
        videoUrl = videoUrl,
        thumbnail = thumbnail
    )
}
