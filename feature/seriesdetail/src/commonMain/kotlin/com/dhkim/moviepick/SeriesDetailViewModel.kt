package com.dhkim.moviepick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dhkim.common.Language
import com.dhkim.common.RestartableStateFlow
import com.dhkim.common.SeriesDetail
import com.dhkim.common.SeriesType
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
class SeriesDetailViewModel(
    private val series: String,
    private val seriesId: String,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getTvDetailUseCase: GetTvDetailUseCase,
) : ViewModel() {

    val uiState: RestartableStateFlow<SeriesDetailUiState> = flowOf(series)
        .flatMapLatest {
            when (series) {
                SeriesType.MOVIE.name -> getMovieDetailUseCase(id = seriesId, language = Language.Korea)
                SeriesType.TV.name -> getTvDetailUseCase(id = seriesId, language = Language.Korea)
                else -> throw IllegalArgumentException("Unknown series type: $series")
            }
        }.flatMapLatest { series ->
            flowOf(createUiState(series))
        }.catch {
            flowOf(
                SeriesDetailUiState(
                    seriesType = SeriesType.entries.first { series == it.name },
                    displayState = SeriesDetailDisplayState.Error(errorCode = "", message = "${it.message}"))
            )
        }
        .onetimeStateIn(
            scope = viewModelScope,
            initialValue = SeriesDetailUiState(
                seriesType = SeriesType.entries.first { series == it.name },
                displayState = SeriesDetailDisplayState.Loading
            )
        )

    private fun createUiState(seriesDetail: SeriesDetail): SeriesDetailUiState {
        val contents = persistentListOf(
            SeriesDetailItem.AppBar(),
            SeriesDetailItem.SeriesDetailPoster(imageUrl = seriesDetail.imageUrl),
            SeriesDetailItem.Information(seriesType = SeriesType.entries.first { it.name == series }, series = seriesDetail),
            SeriesDetailItem.ContentTab(videos = seriesDetail.videos, reviews = flowOf(seriesDetail.review).cachedIn(viewModelScope))
        )

        return SeriesDetailUiState(
            seriesType = SeriesType.entries.first { it.name == series },
            displayState = SeriesDetailDisplayState.Contents(contents)
        )
    }
}