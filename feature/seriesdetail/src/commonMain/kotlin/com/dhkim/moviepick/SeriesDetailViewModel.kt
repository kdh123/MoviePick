package com.dhkim.moviepick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dhkim.common.Date
import com.dhkim.common.Language
import com.dhkim.common.RestartableStateFlow
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesDetail
import com.dhkim.common.SeriesType
import com.dhkim.common.onetimeStateIn
import com.dhkim.common.toAppException
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SeriesDetailViewModel(
    private val series: String,
    private val seriesId: String,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getTvDetailUseCase: GetTvDetailUseCase,
    private val getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
    private val deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase
) : ViewModel() {

    val bookmarks = getSeriesBookmarksUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            initialValue = persistentListOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )
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
            val e = it.toAppException()
            emit(
                SeriesDetailUiState(
                    seriesType = SeriesType.entries.first { type -> series == type.name },
                    displayState = SeriesDetailDisplayState.Error(errorCode = e.code, message = e.message ?: "정보를 불러올 수 없습니다.")
                )
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
        val videoItems = mutableListOf<VideoItem>().apply {
            for (i in 0 until seriesDetail.videos.size) {
                add(seriesDetail.videos[i].toVideoItem(thumbnail = seriesDetail.images[i % seriesDetail.images.size]))
            }
        }.toImmutableList()

        val contents = persistentListOf(
            SeriesDetailItem.AppBar(),
            SeriesDetailItem.SeriesDetailPoster(imageUrl = seriesDetail.images.firstOrNull() ?: ""),
            SeriesDetailItem.Information(seriesType = SeriesType.entries.first { it.name == series }, series = seriesDetail),
            SeriesDetailItem.ContentTab(
                videos = videoItems,
                reviews = flowOf(seriesDetail.review)
                    .cachedIn(viewModelScope)
            )
        )

        return SeriesDetailUiState(
            seriesType = SeriesType.entries.first { it.name == series },
            displayState = SeriesDetailDisplayState.Contents(isUpcoming = Date.isTodayAfter(seriesDetail.openDate), series = contents)
        )
    }

    fun onAction(action: SeriesDetailAction) {
        when (action) {
            is SeriesDetailAction.AddBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.posterUrl,
                            seriesType = seriesType
                        )
                    }
                    addSeriesBookmarkUseCase(seriesEntity)
                }
            }

            is SeriesDetailAction.DeleteBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.posterUrl,
                            seriesType = seriesType
                        )
                    }
                    deleteSeriesBookmarkUseCase(seriesEntity)
                }
            }
        }
    }
}