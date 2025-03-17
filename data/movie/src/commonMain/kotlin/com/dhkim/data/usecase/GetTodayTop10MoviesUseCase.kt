package com.dhkim.data.usecase

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTodayTop10MoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            val nowPlayingMovies: List<Movie> = movieRepository.getNowPlayingMovies(language, region)
                .asSnapshot()
                .take(10)
                .sortedWith(compareByDescending<Movie> { it.voteAverage }.thenByDescending { it.popularity })

            emit(PagingData.from(nowPlayingMovies))
        }
    }
}