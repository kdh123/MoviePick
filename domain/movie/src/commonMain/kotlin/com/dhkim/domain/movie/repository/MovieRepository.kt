package com.dhkim.domain.movie.repository

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>>
    fun getUpcomingMovies(page: Int, language: Language, region: Region): Flow<List<Movie>>
    fun getMovieWithCategory(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Movie>>
    fun getMovieVideos(id: String, language: Language): Flow<List<Video>>
    fun getMovieDetail(id: String, language: Language): Flow<MovieDetail>
    fun getMovieReviews(id: String): Flow<PagingData<Review>>
    fun getMovieActors(id: String, language: Language): Flow<List<String>>
    fun getMovieImages(id: String): Flow<List<SeriesImage>>
    fun searchMovies(query: String, language: Language): Flow<List<Movie>>
}