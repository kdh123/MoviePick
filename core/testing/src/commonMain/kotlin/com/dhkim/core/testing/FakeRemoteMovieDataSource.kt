package com.dhkim.core.testing

import androidx.paging.PagingSource
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.testing.TestPager
import app.cash.paging.testing.asPagingSourceFactory
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.model.MovieGenre
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteMovieDataSource : RemoteMovieDataSource {

    private val topRatedMovies = mutableListOf<Movie>().apply {
        repeat(50) {
            add(
                Movie(
                    id = "topRatedId$it",
                    title = "top rated title$it",
                    overview = "overview $it",
                    genre = listOf(MovieGenre.ACTION.genre, MovieGenre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val nowPlayingMovies = mutableListOf<Movie>().apply {
        repeat(50) {
            add(
                Movie(
                    id = "nowPlayingId$it",
                    title = "now playing title$it",
                    overview = "overview $it",
                    genre = listOf(MovieGenre.ACTION.genre, MovieGenre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-02-05",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val upcomingMovies = mutableListOf<Movie>().apply {
        repeat(50) {
            add(
                Movie(
                    id = "upcomingId$it",
                    title = "upcoming title$it",
                    overview = "overview $it",
                    genre = listOf(MovieGenre.ACTION.genre, MovieGenre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-05-12",
                    voteAverage = 4.3 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val topRatedPagingSource = topRatedMovies.asPagingSourceFactory().invoke()
    private val nowPlayingPagingSource = nowPlayingMovies.asPagingSourceFactory().invoke()
    private val upcomingPagingSource = upcomingMovies.asPagingSourceFactory().invoke()

    override fun getTopRatedMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), topRatedPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getNowPlayingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), nowPlayingPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getUpcomingMovies(language: String, region: String): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), upcomingPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }
}