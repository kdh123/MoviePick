package com.dhkim.core.tv.domain.repository

import app.cash.paging.PagingData
import com.dhkim.core.tv.domain.model.Tv
import kotlinx.coroutines.flow.Flow

interface TvRepository {

    fun getAiringTodayTvs(language: String): Flow<PagingData<Tv>>
    fun getOnTheAirTvs(language: String): Flow<PagingData<Tv>>
    fun getTopRatedTvs(language: String): Flow<PagingData<Tv>>
}