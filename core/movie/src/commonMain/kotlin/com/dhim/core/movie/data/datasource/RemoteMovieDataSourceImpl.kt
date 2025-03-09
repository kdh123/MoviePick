package com.dhim.core.movie.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import com.dhim.core.movie.domain.datasource.RemoteMovieDataSource
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class RemoteMovieDataSourceImpl(
    private val apiService: HttpClient
) : RemoteMovieDataSource {

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {

        return Pager(PagingConfig(pageSize = 15)) {
            TopRatedMoviePagingSource(apiService = apiService)
        }.flow
    }
}