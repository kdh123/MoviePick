package com.dhkim.domain.tv.repository

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.model.TvDetail
import kotlinx.coroutines.flow.Flow

interface TvRepository {

    fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>>
    fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>>
    fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>>
    fun getTvWithCategory(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Tv>>
    fun getTvVideos(id: String, language: Language): Flow<List<Video>>
    fun getTvDetail(id: String, language: Language): Flow<TvDetail>
    fun getTvReviews(id: String): Flow<PagingData<Review>>
    fun getTvCastMembers(id: String, language: Language): Flow<List<String>>
    fun getTvImages(id: String): Flow<List<SeriesImage>>
}