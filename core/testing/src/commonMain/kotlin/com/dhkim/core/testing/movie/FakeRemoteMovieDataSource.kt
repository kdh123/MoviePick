package com.dhkim.core.testing.movie

import androidx.paging.PagingSource
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.testing.TestPager
import app.cash.paging.testing.asPagingSourceFactory
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.data.datasource.RemoteMovieDataSource
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieVideo
import com.dhkim.domain.movie.model.MovieVideoType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteMovieDataSource : RemoteMovieDataSource {

    private val movieVideos = mutableListOf<MovieVideo>().apply {
        repeat(10) {
            add(
                MovieVideo(
                    id = "videoId$it",
                    key = "videoKey$it",
                    videoUrl = "videoUrl$it",
                    name = "name$it",
                    type = MovieVideoType.Teaser
                )
            )
        }
    }

    private val topRatedMovies = mutableListOf<Movie>().apply {
        repeat(50) {
            add(
                Movie(
                    id = "topRatedId$it",
                    title = "top rated title$it",
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.HORROR.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble(),
                    video = movieVideos[0]
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
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ROMANCE.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-02-05",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble(),
                    video = movieVideos[0]
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
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.MYSTERY.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-05-12",
                    voteAverage = 4.3 + it.toDouble(),
                    popularity = 45.38 + it.toDouble(),
                    video = movieVideos[0]
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

    override fun getMovieVideos(id: String, language: String): Flow<List<MovieVideo>> {
        return flow {
            emit(movieVideos)
        }
    }

    override fun getMovieWithCategory(language: String, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return flow {
            val moviePagingSource = (topRatedMovies + upcomingMovies + nowPlayingMovies)
                .filter { it.genre.contains(genre?.genre ?: "")}
                .asPagingSourceFactory()
                .invoke()

            val pager = TestPager(PagingConfig(pageSize = 15), moviePagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }
}