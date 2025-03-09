package com.dhim.core.movie.data.repository

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import com.dhim.core.movie.domain.repository.MovieRepository
import com.dhim.core.movie.domain.datasource.RemoteMovieDataSource
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies()
    }
}