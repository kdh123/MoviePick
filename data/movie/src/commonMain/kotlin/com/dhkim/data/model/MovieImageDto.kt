package com.dhkim.data.model

import com.dhkim.common.ImageType
import com.dhkim.common.SeriesImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImageDto(
    val id: Int,
    val backdrops: List<Backdrop>,
) {
    fun toSeriesImages(): List<SeriesImage> {
        return backdrops.map {
            SeriesImage(
                imageUrl = "https://image.tmdb.org/t/p/original${it.filePath}",
                imageType = if (it.aspectRatio >= 1) {
                    ImageType.Landscape
                } else {
                    ImageType.Portrait
                }
            )
        }
    }
}

@Serializable
data class Backdrop(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("file_path")
    val filePath: String,
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val width: Int
)