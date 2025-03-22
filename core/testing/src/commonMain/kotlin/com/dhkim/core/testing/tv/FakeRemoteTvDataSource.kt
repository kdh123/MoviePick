package com.dhkim.core.testing.tv

import androidx.paging.PagingSource
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.testing.TestPager
import app.cash.paging.testing.asPagingSourceFactory
import com.dhkim.common.Genre
import com.dhkim.common.Region
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.model.Tv
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteTvDataSource : RemoteTvDataSource {

    private val airingTodayTvs = mutableListOf<Tv>().apply {
        repeat(50) {
            add(
                Tv(
                    id = "airingTodayId$it",
                    title = "airing today title$it",
                    adult = false,
                    country = Region.Korea.country,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    firstAirDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val onTheAirTvs = mutableListOf<Tv>().apply {
        repeat(50) {
            add(
                Tv(
                    id = "onTheAirId$it",
                    title = "on the air title$it",
                    adult = false,
                    country = Region.Korea.country,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    firstAirDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val topRatedTvs = mutableListOf<Tv>().apply {
        repeat(50) {
            add(
                Tv(
                    id = "topRatedId$it",
                    title = "top rated title$it",
                    adult = false,
                    country = Region.Korea.country,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    firstAirDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    private val airingTodayPagingSource = airingTodayTvs.asPagingSourceFactory().invoke()
    private val onTheAirPagingSource = onTheAirTvs.asPagingSourceFactory().invoke()
    private val topRatedPagingSource = topRatedTvs.asPagingSourceFactory().invoke()



    override fun getAiringTodayTvs(language: String): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), airingTodayPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getOnTheAirTvs(language: String): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), onTheAirPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getTopRatedTvs(language: String): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), topRatedPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }
}