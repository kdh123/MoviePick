package com.dhkim.data.model

import com.dhkim.common.Genre
import com.dhkim.domain.movie.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val page: Int,
    val results: List<MovieResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class MovieResult(
    val id: Int,
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    fun toMovie(): Movie {
        return Movie(
            id = "$id",
            title = title,
            adult = adult,
            overview = overview,
            imageUrl = if (posterPath != null) {
                "https://image.tmdb.org/t/p/original$posterPath"
            } else {
                null
            },
            genre = genreIds.map { Genre.seriesGenre(it)?.genre ?: Genre.Unknown.genre },
            voteAverage = voteAverage,
            releasedDate = releaseDate,
            popularity = popularity,
        )
    }
}