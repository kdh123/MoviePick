package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.common.Video
import kotlinx.coroutines.flow.Flow

interface GetMovieVideoUseCase {

    operator fun invoke(id: String, language: Language): Flow<Video?>
}