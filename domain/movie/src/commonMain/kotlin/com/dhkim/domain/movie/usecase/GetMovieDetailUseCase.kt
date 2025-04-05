package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetMovieDetailUseCase {

    operator fun invoke(id: String, language: Language): Flow<Movie>
}