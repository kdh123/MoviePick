package com.dhkim.data.tv.datasource

import androidx.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class RemoteTvDataSourceImpl(
    private val apiService: HttpClient
) : RemoteTvDataSource {

    override fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>> {
        return Pager(PagingConfig(pageSize = 15)) {
            AiringTodayTvPagingSource(apiService, language)
        }.flow
    }

    override fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>> {
        return Pager(PagingConfig(pageSize = 15)) {
            OnTheAirTvPagingSource(apiService, language)
        }.flow
    }

    override fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>> {
        return Pager(PagingConfig(pageSize = 15)) {
            TopRatedTvPagingSource(apiService, language)
        }.flow
    }
}