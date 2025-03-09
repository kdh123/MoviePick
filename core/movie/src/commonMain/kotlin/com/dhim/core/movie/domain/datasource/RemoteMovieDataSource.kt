package com.dhim.core.movie.domain.datasource

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteMovieDataSource {

    fun getTopRatedMovies(): Flow<PagingData<Movie>>
}