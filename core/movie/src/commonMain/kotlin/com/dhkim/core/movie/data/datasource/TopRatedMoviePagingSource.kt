package com.dhkim.core.movie.data.datasource


import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.core.movie.data.model.TopRatedMovieDto
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.network.AppException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal class TopRatedMoviePagingSource(
    private val apiService: HttpClient
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/movie/top_rated")
                }
                parameter("language", "ko-KR")
                parameter("page", nextPageNumber)
            }

            val topRatedMovie = response.body<TopRatedMovieDto>()

            return LoadResult.Page(
                data = topRatedMovie.results.map { it.toTopRatedMovie() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (topRatedMovie.results.isNotEmpty()) {
                    nextPageNumber + 1
                } else null
            )
        } catch (e: UnresolvedAddressException) {
            return LoadResult.Error(AppException(errorCode = 1001, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: SerializationException) {
            return LoadResult.Error(AppException(errorCode = 1002, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: IOException) {
            return LoadResult.Error(AppException(errorCode = 1003, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: Exception) {
            return LoadResult.Error(AppException(errorCode = -1, message = "${e.message}"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}