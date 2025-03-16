package com.dhkim.data.model

import com.dhkim.common.Genre
import com.dhkim.domain.movie.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingMovieDto(
    val dates: Dates,
    val page: Int,
    val results: List<UpcomingMovieResults>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class UpcomingMovieResults(
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
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    fun toUpcomingMovie(): Movie {
        return Movie(
            id = "$id",
            title = title,
            adult = adult,
            overview = overview,
            imageUrl = "https://image.tmdb.org/t/p/original$posterPath",
            genre = genreIds.map { Genre.movieGenre(it).genre },
            voteAverage = voteAverage,
            releasedDate = releaseDate,
            popularity = popularity,
            video = video
        )
    }
}