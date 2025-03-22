package com.dhkim.domain.movie.repository

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getUpcomingMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getMovieVideos(id: String, language: Language): Flow<List<MovieVideo>>
    fun getMovieWithCategory(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Movie>>
}