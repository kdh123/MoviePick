package com.dhkim.core.testing.movie

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import kotlinx.coroutines.flow.Flow

class FakeGetMovieWithCategoryUseCase : GetMovieWithCategoryUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override fun invoke(language: String, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return movieRepository.getMovieWithCategory(language, genre, region)
    }
}