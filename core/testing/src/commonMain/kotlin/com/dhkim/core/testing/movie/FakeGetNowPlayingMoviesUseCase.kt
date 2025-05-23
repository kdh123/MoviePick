package com.dhkim.core.testing.movie

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetNowPlayingMoviesUseCase : GetMoviesUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: Language, region: Region): Flow<PagingData<Movie>> {
        return flow {
            if (currentStatus == MovieStatus.Success) {
                val movies = movieRepository.getTopRatedMovies(language, region).first()
                emit(movies)
            } else {
                throw Exception("now playing movies error")
            }
        }
    }
}