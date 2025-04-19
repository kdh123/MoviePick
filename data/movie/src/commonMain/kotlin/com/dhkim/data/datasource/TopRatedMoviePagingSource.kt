package com.dhkim.data.datasource


import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.common.AppException
import com.dhkim.common.Language
import com.dhkim.common.Region
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

internal class TopRatedMoviePagingSource(
    private val apiService: HttpClient,
    private val language: Language,
    private val region: Region
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/movie/top_rated")
                }
                parameter("language", language.code)
                parameter("region", region.code)
                parameter("page", nextPageNumber)
            }

            val movieDto = response.body<MovieDto>()

            return LoadResult.Page(
                data = movieDto.results.map { it.toMovie() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (movieDto.results.isNotEmpty()) {
                    nextPageNumber + 1
                } else null
            )
        } catch (e: UnresolvedAddressException) {
            return LoadResult.Error(AppException(code = 1001, message = "영화 정보를 불러올 수 없습니다."))
        } catch (e: SerializationException) {
            return LoadResult.Error(AppException(code = 1002, message = "영화 정보를 불러올 수 없습니다."))
        } catch (e: IOException) {
            return LoadResult.Error(AppException(code = 1003, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: Exception) {
            return LoadResult.Error(AppException(code = -1, message = "영화 정보를 불러올 수 없습니다."))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}