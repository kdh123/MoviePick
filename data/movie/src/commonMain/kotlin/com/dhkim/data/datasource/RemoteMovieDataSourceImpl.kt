package com.dhkim.data.datasource

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.data.model.MovieCreditsDto
import com.dhkim.data.model.MovieDetailDto
import com.dhkim.data.model.MovieDto
import com.dhkim.data.model.MovieImageDto
import com.dhkim.data.model.MovieVideoDto
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.movie.model.MovieDetail
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

    override fun getUpcomingMovies(page: Int, language: Language, region: Region): Flow<List<Movie>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/upcoming")
                }
                parameter("page", page)
                parameter("language", language.code)
                parameter("region", region.code)
            }
            val results = response.body<MovieDto>().results
            emit(results.map { it.toMovie() })
        }
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

    override fun getMovieDetail(id: String, language: Language): Flow<MovieDetail> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id")
                }
                parameter("language", language.code)
            }
            val movieDetailDto = response.body<MovieDetailDto>()
            emit(movieDetailDto.toMovieDetail())
        }
    }

    override fun getMovieReviews(id: String): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 10)) {
            MovieReviewPagingSource(apiService, id)
        }.flow
    }

    override fun getMovieActors(id: String, language: Language): Flow<List<String>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id/credits")
                }
                parameter("language", language.code)
            }
            val movieCreditsDto = response.body<MovieCreditsDto>()
            val actors = movieCreditsDto.cast.map { it.name }.distinctBy { it }
            emit(actors)
        }
    }

    override fun getMovieImages(id: String): Flow<List<SeriesImage>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/movie/$id/images")
                }
            }
            val movieImageDto = response.body<MovieImageDto>()
            val images = movieImageDto.toSeriesImages()
            emit(images)
        }
    }

    override fun searchMovies(query: String, language: Language): Flow<List<Movie>> {
        return flow {
            val response = apiService.get {
                url {
                    path("/3/search/movie")
                    parameter("query", query)
                    parameter("language", language.code)
                }
            }
            val results = response.body<MovieDto>().results
            emit(results.map { it.toMovie() })
        }
    }
}
