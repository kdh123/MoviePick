package com.dhkim.data.tv.datasource

import androidx.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.data.tv.model.TvCreditsDto
import com.dhkim.data.tv.model.TvDetailDto
import com.dhkim.data.tv.model.TvDto
import com.dhkim.data.tv.model.TvImageDto
import com.dhkim.data.tv.model.TvVideoDto
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.model.TvDetail
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
                parameter("language", language.code)
            }
            val results = response.body<TvVideoDto>().results
            emit(results.mapNotNull { it.toVideo() })
        }
    }

    override fun getTvDetail(id: String, language: Language): Flow<TvDetail> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/tv/$id")
                }
                parameter("language", language.code)
            }
            val tvDetailDto = response.body<TvDetailDto>()
            emit(tvDetailDto.toTvDetail())
        }
    }

    override fun getTvReviews(id: String): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 10)) {
            TvReviewPagingSource(apiService, id)
        }.flow
    }

    override fun getTvCastMembers(id: String, language: Language): Flow<List<String>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/tv/$id/credits")
                }
                parameter("language", language.code)
            }
            val tvCreditsDto = response.body<TvCreditsDto>()
            val castMembers = tvCreditsDto.cast.map { it.name }.distinctBy { it }
            emit(castMembers)
        }
    }

    override fun getTvImages(id: String): Flow<List<SeriesImage>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/tv/$id/images")
                }
            }
            val tvImageDto = response.body<TvImageDto>()
            val images = tvImageDto.toSeriesImages()
            emit(images)
        }
    }

    override fun searchTv(query: String, language: Language): Flow<List<Tv>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/search/tv")
                }
                parameter("query", query)
                parameter("language", language.code)
            }
            val results = response.body<TvDto>().results
            emit(results.map { it.toTv() })
        }
    }
}