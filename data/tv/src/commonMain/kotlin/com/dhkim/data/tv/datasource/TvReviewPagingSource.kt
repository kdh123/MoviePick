package com.dhkim.data.tv.datasource

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.common.Review
import com.dhkim.core.network.AppException
import com.dhkim.data.tv.model.TvReviewDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal class TvReviewPagingSource(
    private val apiService: HttpClient,
    private val movieId: String,
) : PagingSource<Int, Review>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/tv/$movieId/reviews")
                }
                parameter("page", nextPageNumber)
            }

            val movieReviewDto = response.body<TvReviewDto>()

            return LoadResult.Page(
                data = movieReviewDto.results.map { it.toReview() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (movieReviewDto.results.isNotEmpty()) {
                    nextPageNumber + 1
                } else null
            )
        } catch (e: UnresolvedAddressException) {
            return LoadResult.Error(AppException(errorCode = 1001, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        } catch (e: SerializationException) {
            return LoadResult.Error(AppException(errorCode = 1002, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        } catch (e: IOException) {
            return LoadResult.Error(AppException(errorCode = 1003, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: Exception) {
            return LoadResult.Error(AppException(errorCode = -1, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}