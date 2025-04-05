package com.dhkim.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.Video
import com.dhkim.data.model.MovieDetailDto
import com.dhkim.data.model.MovieVideoDto
import com.dhkim.domain.movie.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteMovieDataSourceImpl(
    private val apiService: HttpClient
) : RemoteMovieDataSource {

    override fun getTopRatedMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            TopRatedMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getNowPlayingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            NowPlayingMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getUpcomingMovies(language: Language, region: Region): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            UpcomingMoviePagingSource(apiService, language, region)
        }.flow
    }

    override fun getMovieVideos(id: String, language: Language): Flow<List<Video>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id/videos")
                }
                parameter("language", language)
            }
            val results = response.body<MovieVideoDto>().results
            emit(results.mapNotNull { it.toVideo() })
        }
    }

    override fun getMovieWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 15)) {
            MovieWithCategoryPagingSource(apiService, language, genre, region)
        }.flow
    }

    override fun getMovieDetail(id: String, language: Language): Flow<Movie> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id")
                }
                parameter("language", language)
            }
            val movieDetailDto = response.body<MovieDetailDto>()
            emit(movieDetailDto.toMovie())
        }
    }

    override fun getMovieReviews(id: String): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 10)) {
            MovieReviewPagingSource(apiService, id)
        }.flow
    }
}
