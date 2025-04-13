package com.dhkim.domain.tv.model

import app.cash.paging.PagingData
import com.dhkim.common.Review
import com.dhkim.common.SeriesDetail
import com.dhkim.common.Video

data class TvDetail(
    override val id: String,
    override val images: List<String> = listOf(),
    override val title: String,
    override val adult: Boolean,
    override val overview: String,
    override val country: String,
    override val genre: List<String>,
    override val popularity: Double,
    override val productionCompany: String,
    override val actors: List<String> = listOf(),
    override val review: PagingData<Review> = PagingData.empty(),
    override val videos: List<Video> = listOf(),
    override val openDate: String,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
) : SeriesDetail
