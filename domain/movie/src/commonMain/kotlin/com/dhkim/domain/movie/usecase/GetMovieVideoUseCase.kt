package com.dhkim.domain.movie.usecase

import com.dhkim.domain.movie.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface GetMovieVideoUseCase {

    operator fun invoke(id: String, language: String): Flow<MovieVideo?>
}