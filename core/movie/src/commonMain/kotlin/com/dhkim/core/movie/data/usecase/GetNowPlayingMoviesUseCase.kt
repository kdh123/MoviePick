package com.dhkim.core.movie.data.usecase

import app.cash.paging.PagingData
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetNowPlayingMoviesUseCase(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {

    override fun invoke(): Flow<PagingData<Movie>> {
        return flow {
            val movies = PagingData.from(movieRepository.getNowPlayingMovies().first())
            emit(movies)
        }
    }
}