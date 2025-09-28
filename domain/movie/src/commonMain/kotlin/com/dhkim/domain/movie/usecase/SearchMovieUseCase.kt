package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class SearchMovieUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(query: String): Flow<List<Movie>> {
        if (query.isBlank()) return emptyFlow()

        val language = if (query.trimStart()[0].code in 'A'.code..'Z'.code
            || query.trimStart()[0].code in 'a'.code..'z'.code
        ) {
            Language.US
        } else {
            Language.Korea
        }

        return movieRepository.searchMovies(query, language)
            .map { movies ->
                movies.filter { it.imageUrl != null }
            }
    }
}