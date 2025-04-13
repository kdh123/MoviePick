package com.dhkim.core.testing.movie

import androidx.paging.PagingSource
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.testing.TestPager
import app.cash.paging.testing.asPagingSourceFactory
import com.dhkim.common.Genre
import com.dhkim.common.ImageType
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.common.VideoType
import com.dhkim.data.datasource.RemoteMovieDataSource
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeRemoteMovieDataSource : RemoteMovieDataSource {

    private val movieImages = mutableListOf<SeriesImage>().apply {
        repeat(10) {
            add(
                SeriesImage(
                    imageUrl = "imageUrl$it",
                    imageType = ImageType.Landscape
                )
            )
        }
    }

    private val movieVideos = mutableListOf<Video>().apply {
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

    private val topRatedMovies = mutableListOf<Movie>().apply {
        repeat(50) {
            add(
                Movie(
                    id = "topRatedId$it",
                    title = "top rated title$it",
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ADVENTURE, Genre.HORROR).map { it.genre },
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
                    genre = listOf(Genre.ROMANCE, Genre.DRAMA, Genre.ACTION).map { it.genre },
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
                    genre = listOf(Genre.ACTION, Genre.ROMANCE, Genre.THRILLER, Genre.ANIMATION).map { it.genre },
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-05-12",
                    voteAverage = 4.3 + it.toDouble(),
                    popularity = 45.38 + it.toDouble(),
                    video = movieVideos[0]
                )
            )
        }
    }

    private val movieReviews = mutableListOf<Review>().apply {
        repeat(50) {
            add(
                Review(
                    id = "$it",
                    author = "author$it",
                    createdAt = "2025-12-24",
                    content = "정말 좋은 영화입니다!",
                    rating = 5.0
                )
            )
        }
    }

    private val topRatedPagingSource = topRatedMovies.asPagingSourceFactory().invoke()
    private val nowPlayingPagingSource = nowPlayingMovies.asPagingSourceFactory().invoke()
    private val upcomingPagingSource = upcomingMovies.asPagingSourceFactory().invoke()
    private val movieReviewPagingSource = movieReviews.asPagingSourceFactory().invoke()

    override fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), topRatedPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), nowPlayingPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getUpcomingMovies(page: Int, language: Language, region: Region): Flow<List<Movie>> {
        return flow {
            emit(upcomingMovies)
        }
    }

    override fun getMovieVideos(id: String, language: Language): Flow<List<Video>> {
        return flow {
            emit(movieVideos)
        }
    }

    override fun getMovieWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        val movieWithCategoryPagingSource = (topRatedMovies + upcomingMovies + nowPlayingMovies)
            .filter { it.genre.contains(genre?.genre) || it.genre.contains(region?.country) }
            .asPagingSourceFactory().invoke()

        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), movieWithCategoryPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getMovieDetail(id: String, language: Language): Flow<MovieDetail> {
        return flow {
            val movie = (topRatedMovies + upcomingMovies + nowPlayingMovies).firstOrNull { it.id == id }
            if (movie != null) {
                val movieDetail = MovieDetail(
                    id = movie.id,
                    images = listOf(movie.imageUrl),
                    title = movie.title,
                    adult = movie.adult,
                    overview = movie.overview,
                    genre = movie.genre,
                    openDate = movie.releasedDate,
                    popularity = movie.popularity,
                    runtime = 90,
                    productionCompany = "Disney",
                    country = "미국",
                    videos = movieVideos,
                    review = getMovieReviews(id).first()
                )
                emit(movieDetail)
            }
        }
    }

    override fun getMovieReviews(id: String): Flow<PagingData<Review>> {
        return flow {
            val pager = TestPager(PagingConfig(pageSize = 15), movieReviewPagingSource)
            val page = with(pager) {
                refresh()
                append()
            } as PagingSource.LoadResult.Page
            emit(PagingData.from(page.data))
        }
    }

    override fun getMovieActors(id: String, language: Language): Flow<List<String>> {
        return flow {
            emit(listOf("배우1", "배우2", "배우3"))
        }
    }

    override fun getMovieImages(id: String): Flow<List<SeriesImage>> {
        return flow {
            emit(movieImages)
        }
    }
}