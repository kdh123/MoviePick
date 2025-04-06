package com.dhkim.domain.movie.model

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.common.Video

data class MovieDetail(
    val id: String,
    val title: String,
    val adult: Boolean,
    val overview: String,
    val imageUrl: String,
    val country: String,
    val genre: List<String>,
    val popularity: Double,
    val runtime: Int,
    val releasedDate: String,
    val productionCompany: String,
    val actors: List<String> = listOf(),
    val review: PagingData<Review> = PagingData.empty(),
    val videos: List<Video> = listOf(),
)
