package com.dhkim.core.movie.domain.repository

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(): Flow<PagingData<Movie>>
}