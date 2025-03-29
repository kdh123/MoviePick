package com.dhkim.data.repository

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Video
import com.dhkim.data.datasource.RemoteMovieDataSource
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies(language, region)
    }

    override fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return flow {
            val nowPlayingMovies = remoteMovieDataSource.getNowPlayingMovies(language, region).first()
            emit(nowPlayingMovies)
        }
    }

    override fun getUpcomingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getUpcomingMovies(language, region)
    }

    override fun getMovieVideos(id: String, language: Language): Flow<List<Video>> {
        return remoteMovieDataSource.getMovieVideos(id, language)
    }

    override fun getMovieWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getMovieWithCategory(language, genre, region)
    }
}