package com.dhkim.domain.tv.usecase

import com.dhkim.common.Language
import com.dhkim.common.Video
import kotlinx.coroutines.flow.Flow

interface GetTvVideoUseCase {

    operator fun invoke(id: String, language: Language): Flow<Video?>
}