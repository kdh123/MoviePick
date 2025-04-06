package com.dhkim.domain.tv.usecase

import com.dhkim.common.Language
import com.dhkim.domain.tv.model.TvDetail
import kotlinx.coroutines.flow.Flow

interface GetTvDetailUseCase {

    operator fun invoke(id: String, language: Language): Flow<TvDetail>
}