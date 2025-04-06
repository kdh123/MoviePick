package com.dhkim.core.testing.tv

import androidx.paging.PagingSource
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.testing.TestPager
import app.cash.paging.testing.asPagingSourceFactory
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.Video
import com.dhkim.common.VideoType
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.model.TvDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteTvDataSource : RemoteTvDataSource {

    private val tvVideos = mutableListOf<Video>().apply {
        repeat(10) {
            add(
                Video(
                    id = "videoId$it",
                    key = "videoKey$it",
                    videoUrl = "videoUrl$it",
                    name = "name$it",
                    type = VideoType.Teaser
                )
            )
        }
    }

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

    private val tvReviews = mutableListOf<Review>().apply {
        repeat(50) {
            add(
                Review(
                    id = "$it",
                    author = "author$it",
                    createdAt = "2025-12-24",
                    content = "정말 좋은 프로그램입니다!",
                    rating = 5.0
                )
            )
        }
    }

    private val airingTodayPagingSource = airingTodayTvs.asPagingSourceFactory().invoke()
    private val onTheAirPagingSource = onTheAirTvs.asPagingSourceFactory().invoke()
    private val topRatedPagingSource = topRatedTvs.asPagingSourceFactory().invoke()
    private val tvWithCategoryPagingSource = (airingTodayTvs + onTheAirTvs + topRatedTvs)
        .filter { it.genre.contains(Genre.ROMANCE.genre) || it.genre.contains(Genre.DRAMA.genre)}
        .asPagingSourceFactory().invoke()
    private val tvReviewPagingSource = tvReviews.asPagingSourceFactory().invoke()


    override fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), airingTodayPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), onTheAirPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), topRatedPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getTvWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Tv>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), tvWithCategoryPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getTvVideos(id: String, language: Language): Flow<List<Video>> {
        return flow {
            emit(tvVideos)
        }
    }

    override fun getTvDetail(id: String, language: Language): Flow<TvDetail> {
        return flow {
            val tv = (topRatedTvs + airingTodayTvs + onTheAirTvs).firstOrNull { it.id == id }
            if (tv != null) {
                val tvDetail = TvDetail(
                    id = tv.id,
                    title = tv.title,
                    adult = tv.adult,
                    overview = tv.overview,
                    imageUrl = tv.imageUrl,
                    country = tv.country,
                    genre = tv.genre,
                    popularity = tv.popularity,
                    firstAirDate = tv.firstAirDate,
                    productionCompany = "Netflix",
                    numberOfEpisodes = 10,
                    numberOfSeasons = 2,
                    actors = listOf("배우1", "배우2", "배우3"),
                    review = PagingData.from(tvReviews),
                    videos = tvVideos,
                )
                emit(tvDetail)
            }
        }
    }

    override fun getTvReviews(id: String): Flow<PagingData<Review>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), tvReviewPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getTvCastMembers(id: String, language: Language): Flow<List<String>> {
        return flow {
            emit(listOf("멤버1", "멤버2", "멤버3"))
        }
    }
}