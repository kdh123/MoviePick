package com.dhkim.data.tv.datasource

import androidx.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.Video
import com.dhkim.data.tv.model.TvDetailDto
import com.dhkim.data.tv.model.TvVideoDto
import com.dhkim.domain.tv.model.Tv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override fun getTvWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Tv>> {
        return Pager(PagingConfig(pageSize = 15)) {
            TvWithCategoryPagingSource(apiService, language, genre, region)
        }.flow
    }

    override fun getTvVideos(id: String, language: Language): Flow<List<Video>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/tv/$id/videos")
                }
                parameter("language", language)
            }
            val results = response.body<TvVideoDto>().results
            emit(results.mapNotNull { it.toVideo() })
        }
    }

    override fun getTvDetail(id: String, language: Language): Flow<Tv> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/tv/$id")
                }
                parameter("language", language)
            }
            val movieDetailDto = response.body<TvDetailDto>()
            emit(movieDetailDto.toTv())
        }
    }

    override fun getTvReviews(id: String): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 10)) {
            TvReviewPagingSource(apiService, id)
        }.flow
    }
}