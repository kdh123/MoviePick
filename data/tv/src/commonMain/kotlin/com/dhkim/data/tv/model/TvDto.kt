package com.dhkim.data.tv.model

import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.domain.tv.model.Tv
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TvDto(
    val page: Int,
    val results: List<TvResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
internal data class TvResult(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    val name: String,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_name")
    val originalName: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    fun toTv(): Tv {
        return Tv(
            id = "$id",
            title = name,
            adult = adult,
            country = if (originCountry.isNotEmpty()) {
                Region.entries.firstOrNull { it.code == originCountry[0] }?.country ?: Region.Unknown.country
            } else {
                Region.Unknown.country
            },
            overview = overview,
            imageUrl = "https://image.tmdb.org/t/p/original${posterPath}",
            genre = genreIds.map { Genre.seriesGenre(it)?.genre ?: Genre.Unknown.genre },
            voteAverage = voteAverage,
            firstAirDate = firstAirDate,
            popularity = popularity
        )
    }
}