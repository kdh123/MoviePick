package com.dhkim.domain.series.usecase

import com.dhkim.common.SeriesBookmark
import com.dhkim.domain.series.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSeriesBookmarksUseCaseImpl(
    private val seriesRepository: SeriesRepository
) : GetSeriesBookmarksUseCase {

    override fun invoke(): Flow<List<SeriesBookmark>> {
        return seriesRepository.getBookmarks()
            .map { it.reversed() }
    }
}