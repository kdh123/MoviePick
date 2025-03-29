package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import kotlinx.coroutines.flow.Flow

class FakeGetTvWithCategoryUseCase : GetTvWithCategoryUseCase {

    private var currentStatus = TvStatus.Success

    private val tvRepository = FakeTvRepository()

    fun setStatus(status: TvStatus) {
        currentStatus = status
    }

    override fun invoke(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Tv>> {
        return tvRepository.getTvWithCategory(language, genre, region)
    }
}