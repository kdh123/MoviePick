package com.dhkim.core.movie.domain.datasource

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteMovieDataSource {

    fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>>
}