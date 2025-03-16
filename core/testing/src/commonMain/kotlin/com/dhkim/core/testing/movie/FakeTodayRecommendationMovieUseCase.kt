package com.dhkim.core.testing.movie

import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeTodayRecommendationMovieUseCase : GetMoviesUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override operator fun invoke(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            if (currentStatus == MovieStatus.Success) {
                val movies = movieRepository.getTodayRecommendationMovie(language, region).first()
                emit(movies)
            } else {
                throw Exception("today recommendation movie error")
            }
        }
    }
}