package com.dhkim.tv.data.datasource

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.core.network.AppException
import com.dhkim.tv.data.model.TopRatedTvDto
import com.dhkim.tv.domain.model.Tv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal class TopRatedTvPagingSource(
    private val apiService: HttpClient,
    private val language: String,
) : PagingSource<Int, Tv>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tv> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/tv/top_rated")
                }
                parameter("language", language)
                parameter("page", nextPageNumber)
            }

            val topRatedTvDto = response.body<TopRatedTvDto>()

            return LoadResult.Page(
                data = topRatedTvDto.results.map { it.toTopRatedTv() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (topRatedTvDto.results.isNotEmpty()) {
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

    override fun getRefreshKey(state: PagingState<Int, Tv>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}