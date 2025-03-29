package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class GetTvWithCategoryUseCaseImpl(
    private val tvRepository: TvRepository
) : GetTvWithCategoryUseCase {

    override fun invoke(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Tv>> {
        return tvRepository.getTvWithCategory(language, genre, region)
    }
}