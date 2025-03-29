package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Series
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getMoviesUseCase: Map<String, GetMoviesUseCase>,
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
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
                        when (it.second.series) {
                            com.dhkim.home.Series.MOVIE -> {
                                getMoviesUseCase[it.first]!!(language, region).toContent(group = it.second, viewModelScope)
                            }

                            com.dhkim.home.Series.TV -> {
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
            is HomeAction.BackToHome -> {
                _uiState.update {
                    it.copy(
                        displayState = HomeDisplayState.Contents(
                            movies = getCurrentHomeMovies()
                        )
                    )
                }
            }
        }
    }

    private fun getCurrentHomeMovies(): ImmutableList<SeriesItem> {
        if (_uiState.value.displayState !is HomeDisplayState.Contents) return persistentListOf()

        return (_uiState.value.displayState as HomeDisplayState.Contents).movies
    }
}


