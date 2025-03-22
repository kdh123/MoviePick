package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieWithCategoryUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMovieWithCategoryUseCase {

    override fun invoke(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return movieRepository.getMovieWithCategory(language, genre, region)
    }
}