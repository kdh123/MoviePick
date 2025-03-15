package com.dhkim.core.tv.data.model

import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.core.tv.domain.model.Tv
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OnTheAirTvDto(
    val page: Int,
    val results: List<OnTheAirTvResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
internal data class OnTheAirTvResult(
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
    val posterPath: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    fun toOnTheAirTv(): Tv {
        return Tv(
            id = "$id",
            title = name,
            adult = adult,
            country = Region.entries.firstOrNull { it.code == originCountry[0] }?.country ?: Region.Unknown.country,
            overview = overview,
            imageUrl = "https://image.tmdb.org/t/p/original$posterPath",
            genre = genreIds.map { Genre.movieGenre(it).genre },
            voteAverage = voteAverage,
            firstAirDate = firstAirDate,
            popularity = popularity
        )
    }
}