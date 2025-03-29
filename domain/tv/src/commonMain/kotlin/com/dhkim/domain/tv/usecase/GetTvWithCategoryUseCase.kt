package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.tv.model.Tv
import kotlinx.coroutines.flow.Flow

interface GetTvWithCategoryUseCase {

    operator fun invoke(language: Language, genre: Genre? = null, region: Region? = null): Flow<PagingData<Tv>>
}