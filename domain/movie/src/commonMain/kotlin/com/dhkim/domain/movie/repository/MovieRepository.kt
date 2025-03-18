package com.dhkim.domain.movie.repository

import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>>
    fun getMovieVideos(id: String, language: String): Flow<List<MovieVideo>>
}