package com.dhkim.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.data.model.MovieVideoDto
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieVideo
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

    override fun getMovieVideos(id: String, language: String): Flow<List<MovieVideo>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id/videos")
                }
                parameter("language", language)
            }
            val results = response.body<MovieVideoDto>().results
            emit(results.mapNotNull { it.toMovieVideo() })
        }
    }

    override fun getMovieWithCategory(language: String, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            MovieWithCategoryPagingSource(apiService, language, "${genre?.id}", region?.code)
        }.flow
    }
}
