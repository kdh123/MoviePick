package com.dhkim.core.movie.domain.repository

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>>
}