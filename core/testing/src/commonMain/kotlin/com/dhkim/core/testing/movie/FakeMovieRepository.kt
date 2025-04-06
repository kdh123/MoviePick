package com.dhkim.core.testing.movie


import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.Video
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieDetail
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository : MovieRepository {

    private val remoteMovieDataSource = FakeRemoteMovieDataSource()

    override fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getTopRatedMovies(language, region)
    }

    override fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getNowPlayingMovies(language, region)
    }

    override fun getUpcomingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getUpcomingMovies(language, region)
    }

    override fun getMovieVideos(id: String, language: Language): Flow<List<Video>> {
        return remoteMovieDataSource.getMovieVideos(id, language)
    }

    override fun getMovieDetail(id: String, language: Language): Flow<MovieDetail> {
        return remoteMovieDataSource.getMovieDetail(id, language)
    }

    override fun getMovieReviews(id: String): Flow<PagingData<Review>> {
        return remoteMovieDataSource.getMovieReviews(id)
    }

    override fun getMovieActors(id: String, language: Language): Flow<List<String>> {
        return remoteMovieDataSource.getMovieActors(id, language)
    }

    override fun getMovieWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return remoteMovieDataSource.getMovieWithCategory(language, genre, region)
    }
}