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

    private val movies = mutableListOf<Movie>().apply {
        repeat(50) {
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

    private val pagingSourceFactory = movies.asPagingSourceFactory()
    private val pagingSource = pagingSourceFactory()

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), pagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }
}