package com.dhkim.core.movie.domain.datasource

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteMovieDataSource {

    fun getTopRatedMovies(): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(): Flow<List<Movie>>
}