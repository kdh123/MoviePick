package com.dhkim.domain.tv.repository

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Video
import com.dhkim.domain.tv.model.Tv
import kotlinx.coroutines.flow.Flow

interface TvRepository {

    fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>>
    fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>>
    fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>>
    fun getTvWithCategory(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Tv>>
    fun getTvVideos(id: String, language: Language): Flow<List<Video>>
}