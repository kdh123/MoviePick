package com.dhkim.data.tv.repository

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class TvRepositoryImpl(
    private val remoteTvDataSource: RemoteTvDataSource
) : TvRepository {

    override fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getAiringTodayTvs(language)
    }

    override fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getOnTheAirTvs(language)
    }

    override fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getTopRatedTvs(language)
    }
}