package com.dhkim.domain.tv.usecase

import com.dhkim.common.Language
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class SearchTvUseCase(
    private val tvRepository: TvRepository
) {

    operator fun invoke(query: String): Flow<List<Tv>> {
        if (query.isBlank()) return emptyFlow()

        val language = if (query.trimStart()[0].code in 'A'.code..'Z'.code
            || query.trimStart()[0].code in 'a'.code..'z'.code
        ) {
            Language.US
        } else {
            Language.Korea
        }

        return tvRepository.searchTv(query, language)
            .map { tvs ->
                tvs.filter { it.imageUrl != null }
            }
    }
}