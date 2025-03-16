package com.dhkim.data.usecase

import app.cash.paging.PagingData
import app.cash.paging.map
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetTodayRecommendationMovieUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(language: String, region: String): Flow<PagingData<Movie>> {
        return movieRepository.getTodayRecommendationMovie(language, region)
    }
}