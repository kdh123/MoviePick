package com.dhkim.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.data.model.NowPlayingMovieDto
import com.dhkim.domain.movie.datasource.RemoteMovieDataSource
import com.dhkim.domain.movie.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteMovieDataSourceImpl(
    private val apiService: HttpClient
) : RemoteMovieDataSource {

    override fun getTodayRecommendationMovie(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            apiService.get {
                val response = apiService.get {
                    url {
                        path("/3/movie/now_playing")
                    }
                    parameter("language", language)
                    parameter("region", region)
                    parameter("page", 1)
                }

                val nowPlayingMovieDto = response.body<NowPlayingMovieDto>()
                val recommendationMovie = nowPlayingMovieDto.results.maxBy { it.popularity }.toNowPlayingMovie()
                emit(PagingData.from(listOf(recommendationMovie)))
            }
        }
    }

    override fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            TopRatedMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            NowPlayingMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            UpcomingMoviePagingSource(apiService, language, region)
        }.flow
    }
}