package com.dhkim.core.movie.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.core.movie.data.model.NowPlayingMovieDto
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
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

    override fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            TopRatedMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getNowPlayingMovies(language: String, region: String): Flow<List<Movie>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/now_playing")
                }
                parameter("language", "ko-KR")
                parameter("region", region)
            }

            val nowPlayingMovies = response.body<NowPlayingMovieDto>()
            emit(nowPlayingMovies.results.map { it.toNowPlayingMovie() })
        }
    }

    override fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            UpcomingMoviePagingSource(apiService, language, region)
        }.flow
    }
}