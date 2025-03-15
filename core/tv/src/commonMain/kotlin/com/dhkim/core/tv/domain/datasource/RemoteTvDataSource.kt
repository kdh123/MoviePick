package com.dhkim.core.tv.domain.datasource

import app.cash.paging.PagingData
import com.dhkim.core.tv.domain.model.Tv
import kotlinx.coroutines.flow.Flow

interface RemoteTvDataSource {

    fun getAiringTodayTvs(language: String): Flow<PagingData<Tv>>
    fun getOnTheAirTvs(language: String): Flow<PagingData<Tv>>
    fun getTopRatedTvs(language: String): Flow<PagingData<Tv>>
}