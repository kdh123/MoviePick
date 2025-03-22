package com.dhkim.data.datasource

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.core.network.AppException
import com.dhkim.data.model.MovieDto
import com.dhkim.domain.movie.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal class MovieWithCategoryPagingSource(
    private val apiService: HttpClient,
    private val language: Language,
    private val genre: Genre?,
    private val region: Region?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/discover/movie")
                }
                parameter("language", language)
                parameter("with_genres", genre?.id)
                parameter("region", region?.code)
            }

            val nowPlayingMovieDto = response.body<MovieDto>()

            return LoadResult.Page(
                data = nowPlayingMovieDto.results.map { it.toMovie() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nowPlayingMovieDto.results.isNotEmpty()) {
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