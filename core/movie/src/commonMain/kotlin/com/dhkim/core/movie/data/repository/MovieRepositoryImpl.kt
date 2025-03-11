package com.dhkim.core.movie.data.repository

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies()
    }
}