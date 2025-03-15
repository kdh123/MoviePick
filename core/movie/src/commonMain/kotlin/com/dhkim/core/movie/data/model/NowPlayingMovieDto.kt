package com.dhkim.core.movie.data.model

import com.dhkim.common.Genre
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NowPlayingMovieDto(
    @SerialName("dates")
    val dates: Dates,
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<NowPlayingMovieResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class NowPlayingMovieResult(
    @SerialName("id")
    val id: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    fun toNowPlayingMovie(): Movie {
        return Movie(
            id = "$id",
            title = title,
            adult = adult,
            overview = overview,
            imageUrl = "https://image.tmdb.org/t/p/original$posterPath",
            genre = genreIds.map { Genre.movieGenre(it).genre },
            voteAverage = voteAverage,
            releasedDate = releaseDate,
            popularity = popularity
        )
    }
}