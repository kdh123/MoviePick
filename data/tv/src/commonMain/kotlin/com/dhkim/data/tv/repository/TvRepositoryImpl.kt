package com.dhkim.data.tv.repository

import app.cash.paging.PagingData
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class TvRepositoryImpl(
    private val remoteTvDataSource: RemoteTvDataSource
) : TvRepository {

    override fun getAiringTodayTvs(language: String): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getAiringTodayTvs(language)
    }

    override fun getOnTheAirTvs(language: String): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getOnTheAirTvs(language)
    }

    override fun getTopRatedTvs(language: String): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getTopRatedTvs(language)
    }
}