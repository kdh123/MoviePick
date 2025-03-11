package com.dhkim.core.movie.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedMovieDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<TopRatedMovieResult>,
    @SerialName("total_pages")
    val total_pages: Int,
    @SerialName("total_results")
    val total_results: Int
)