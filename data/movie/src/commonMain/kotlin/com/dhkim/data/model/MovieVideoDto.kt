package com.dhkim.data.model

import com.dhkim.domain.movie.model.MovieVideo
import com.dhkim.domain.movie.model.MovieVideoType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoDto(
    val id: Int,
    val results: List<MovieVideoResult>
)

@Serializable
data class MovieVideoResult(
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
    fun toMovieVideo(): MovieVideo? {
        return if (site == "YouTube") {
            MovieVideo(
                id = id,
                key = key,
                videoUrl = "https://www.youtube.com/watch?v=$key",
                name = name,
                type = MovieVideoType.entries.find { it.name == type } ?: MovieVideoType.Teaser
            )
        } else {
            null
        }
    }
}