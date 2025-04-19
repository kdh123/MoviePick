package com.dhkim.data.tv.datasource

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.dhkim.common.AppException
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.data.tv.model.TvDto
import com.dhkim.domain.tv.model.Tv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal class TvWithCategoryPagingSource(
    private val apiService: HttpClient,
    private val language: Language,
    private val genre: Genre?,
    private val region: Region?
) : PagingSource<Int, Tv>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tv> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.get {
                url {
                    path("/3/discover/tv")
                }
                parameter("language", language.code)
                parameter("with_genres", genre?.id)
                parameter("with_origin_country", region?.code)
                parameter("page", nextPageNumber)
            }

            val nowPlayingMovieDto = response.body<TvDto>()

            return LoadResult.Page(
                data = nowPlayingMovieDto.results.map { it.toTv() },
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nowPlayingMovieDto.results.isNotEmpty()) {
                    nextPageNumber + 1
                } else null
            )
        } catch (e: UnresolvedAddressException) {
            return LoadResult.Error(AppException(code = 1001, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        } catch (e: SerializationException) {
            return LoadResult.Error(AppException(code = 1002, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        } catch (e: IOException) {
            return LoadResult.Error(AppException(code = 1003, message = "네트워크 연결 상태가 좋지 않습니다."))
        } catch (e: Exception) {
            return LoadResult.Error(AppException(code = -1, message = "TV 프로그램 정보를 불러올 수 없습니다."))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Tv>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}