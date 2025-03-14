package com.dhkim.core.testing


import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository : MovieRepository {

    private val remoteMovieDataSource = FakeRemoteMovieDataSource()

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies()
    }

    override fun getNowPlayingMovies(): Flow<List<Movie>> {
        return remoteMovieDataSource.getNowPlayingMovies()
    }
}