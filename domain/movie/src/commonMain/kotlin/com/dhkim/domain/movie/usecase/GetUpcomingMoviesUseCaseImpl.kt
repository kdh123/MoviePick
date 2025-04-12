package com.dhkim.domain.movie.usecase

import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GetUpcomingMoviesUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetUpcomingMoviesUseCase {

    override fun invoke(language: Language, region: Region): Flow<List<Movie>> {
        val currentMoment = Clock.System.now()
        val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        val now = Instant.parse("${currentDateTime.date}T12:00:00Z")

        return combine(
            movieRepository.getUpcomingMovies(page = 1, language, region),
            movieRepository.getUpcomingMovies(page = 2, language, region),
        ) { movies1, movies2 ->
            val m1 = movies1.filter { now < Instant.parse("${it.releasedDate}T12:00:00Z") }
            val m2 = movies2.filter { now < Instant.parse("${it.releasedDate}T12:00:00Z") }

            m1 + m2
        }
    }
}