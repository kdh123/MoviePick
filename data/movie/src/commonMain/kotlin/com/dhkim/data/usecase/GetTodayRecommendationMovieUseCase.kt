package com.dhkim.data.usecase

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetTodayRecommendationMovieUseCase(
    private val movieRepository: MovieRepository,
    private val getMovieVideoUseCase: GetMovieVideoUseCase
) : GetMoviesUseCase {

    override fun invoke(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            val index = (0 until 10).random()
            val nowPlayingMovies = movieRepository.getNowPlayingMovies(language, region)
                .asSnapshot()
                .take(10)
            val topRatedMovies = movieRepository.getTopRatedMovies(language, region)
                .asSnapshot()
                .take(10)
            val movie = (nowPlayingMovies)[index]
            val video = getMovieVideoUseCase(movie.id, language).first()
            emit(PagingData.from(listOf(movie.copy(video = video))))
        }
    }
}