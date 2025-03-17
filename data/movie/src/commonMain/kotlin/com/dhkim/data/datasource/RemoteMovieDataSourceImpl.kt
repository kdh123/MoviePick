package com.dhkim.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.domain.movie.datasource.RemoteMovieDataSource
import com.dhkim.domain.movie.model.Movie
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

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
}