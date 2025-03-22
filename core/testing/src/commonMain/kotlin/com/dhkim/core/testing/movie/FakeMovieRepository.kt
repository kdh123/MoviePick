package com.dhkim.core.testing.movie


import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieVideo
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository : MovieRepository {

    private val remoteMovieDataSource = FakeRemoteMovieDataSource()

    override fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies(language, region)
    }

    override fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getNowPlayingMovies(language, region)
    }

    override fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getUpcomingMovies(language, region)
    }

    override fun getMovieVideos(id: String, language: String): Flow<List<MovieVideo>> {
        return remoteMovieDataSource.getMovieVideos(id, language)
    }

    override fun getMovieWithCategory(language: String, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getMovieWithCategory(language, genre, region)
    }
}