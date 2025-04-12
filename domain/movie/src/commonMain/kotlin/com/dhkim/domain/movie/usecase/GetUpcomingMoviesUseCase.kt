package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetUpcomingMoviesUseCase {

    operator fun invoke(language: Language, region: Region): Flow<List<Movie>>
}