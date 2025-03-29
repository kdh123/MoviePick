package com.dhkim.data.tv.model

import com.dhkim.common.Video
import com.dhkim.common.VideoType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvVideoDto(
    val id: Int,
    val results: List<TvVideoResult>
)

@Serializable
data class TvVideoResult(
    val id: String,
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    val key: String,
    val name: String,
    val official: Boolean,
    @SerialName("published_at")
    val publishedAt: String,
    val site: String,
    val size: Int,
    val type: String
) {
    fun toVideo(): Video? {
        return if (site == "YouTube") {
            Video(
                id = id,
                key = key,
                videoUrl = "https://www.youtube.com/watch?v=$key",
                name = name,
                type = VideoType.entries.find { it.name == type } ?: VideoType.Teaser
            )
        } else {
            null
        }
    }
}