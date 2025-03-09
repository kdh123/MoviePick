package com.dhkim.core.testing


import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import com.dhim.core.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository : MovieRepository {

    private val remoteMovieDataSource = FakeRemoteMovieDataSource()

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies()
    }
}