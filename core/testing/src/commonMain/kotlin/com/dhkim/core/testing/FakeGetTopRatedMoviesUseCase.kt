package com.dhkim.core.testing

import app.cash.paging.PagingData
import com.dhim.core.movie.domain.model.Movie
import com.dhim.core.movie.domain.model.MovieGenre
import com.dhim.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeGetTopRatedMoviesUseCase : GetMoviesUseCase {

    private var currentStatus = MovieStatus.Success

    private val movieRepository = FakeMovieRepository()

    fun setStatus(status: MovieStatus) {
        currentStatus = status
    }

    override operator fun invoke(): Flow<PagingData<Movie>> {
        val movies = mutableListOf<Movie>().apply {
            repeat(10) {
                add(
                    Movie(
                        id = "id$it",
                        title = "title$it",
                        overview = "overview $it",
                        genre = listOf(MovieGenre.ACTION.genre, MovieGenre.DRAMA.genre),
                        imageUrl = "imageUrl$it"
                    )
                )
            }
        }

        return flow {
            if (currentStatus == MovieStatus.Success) {
                emit(movieRepository.getTopRatedMovies().first())
            } else {
                throw Exception("error")
            }
        }
    }
}