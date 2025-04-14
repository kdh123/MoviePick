package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getMoviesUseCase: Map<String, GetMoviesUseCase>,
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.onStart {
        init()
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = HomeUiState()
    )

    private fun init() {
        val language = Language.Korea
        val region = Region.Korea

        viewModelScope.handle(
            block = {
                val jobs = mutableListOf<Deferred<SeriesItem.Content>>()
                listOf(
                    TODAY_RECOMMENDATION_MOVIE_KEY to Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE,
                    TODAY_TOP_10_MOVIES_KEY to Group.HomeGroup.TODAY_TOP_10_MOVIES,
                    TOP_RATED_MOVIES_KEY to Group.HomeGroup.TOP_RATED_MOVIE,
                    NOW_PLAYING_MOVIES_KEY to Group.HomeGroup.NOW_PLAYING_MOVIE,
                    AIRING_TODAY_TVS_KEY to Group.HomeGroup.AIRING_TODAY_TV,
                    ON_THE_AIR_TVS_KEY to Group.HomeGroup.ON_THE_AIR_TV,
                    TOP_RATED_TVS_KEY to Group.HomeGroup.TOP_RATED_TV
                ).map {
                    async {
                        when (it.second.seriesType) {
                            SeriesType.MOVIE -> {
                                getMoviesUseCase[it.first]!!(language, region).toContent(group = it.second, viewModelScope)
                            }

                            SeriesType.TV -> {
                                getTvsUseCase[it.first]!!(language).toContent(group = it.second, viewModelScope)
                            }

                            null -> throw Exception("Series is null")
                        }
                    }
                }.forEach { job ->
                    jobs.add(job)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.HomeGroup.APP_BAR),
                    SeriesItem.Category(group = Group.HomeGroup.CATEGORY)
                ) + jobs.awaitAll()
                    .map { SeriesItem.Content(it.group, it.series) }

                _uiState.update {
                    HomeUiState(displayState = HomeDisplayState.Contents(movies = series.toImmutableList()))
                }
            },
            error = {
                val errorMessage = it.message ?: ""
                _uiState.update { state ->
                    state.copy(displayState = HomeDisplayState.Error(errorCode = "300", message = errorMessage))
                }
            }
        )
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.AddBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action.series) {
                        SeriesBookmark(
                            id = id,
                            title = title,
                            imageUrl = imageUrl
                        )
                    }
                    addSeriesBookmarkUseCase(seriesEntity)
                }
            }
        }
    }
}


