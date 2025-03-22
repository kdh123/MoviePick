package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetMovieWithCategoryUseCase {

    operator fun invoke(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Movie>>
}