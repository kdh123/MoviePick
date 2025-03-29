package com.dhkim.data.datasource

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Video
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteMovieDataSource {

    fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getUpcomingMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getMovieVideos(id: String, language: Language): Flow<List<Video>>
    fun getMovieWithCategory(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Movie>>
}