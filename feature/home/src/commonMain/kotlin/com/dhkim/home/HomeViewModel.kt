package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.filter
import app.cash.paging.map
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Series
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getMoviesUseCase: Map<String, GetMoviesUseCase>,
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
) : ViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(HomeUiState(HomeDisplayState.Loading))
    val uiState: StateFlow<HomeUiState> = _uiState.onStart {
        init()
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = HomeUiState(HomeDisplayState.Loading)
    )

    private fun init() {
        val language = Language.Korea.code
        val region = Region.Korea.code

        viewModelScope.handle(
            block = {
                val jobs = mutableListOf<Deferred<HomeMovieItem>>()
                val todayRecommendationMovie = async {
                    getMoviesUseCase[TODAY_RECOMMENDATION_MOVIE_KEY]!!(language, region)
                        .toHomeMovieItem(group = HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE)
                }

                val topRatedMovies = async {
                    getMoviesUseCase[TOP_RATED_MOVIES_KEY]!!(language, region)
                        .toHomeMovieItem(group = HomeMovieGroup.TOP_RATED_MOVIE)
                }
                val nowPlayingMovies = async {
                    getMoviesUseCase[NOW_PLAYING_MOVIES_KEY]!!(language, region)
                        .toHomeMovieItem(group = HomeMovieGroup.NOW_PLAYING_MOVIE_TOP_10)
                }
                val airingTodayTvs = async {
                    getTvsUseCase[AIRING_TODAY_TVS_KEY]!!(language)
                        .toHomeMovieItem(group = HomeMovieGroup.AIRING_TODAY_TV)
                }
                val onTheAirTvs = async {
                    getTvsUseCase[ON_THE_AIR_TVS_KEY]!!(language)
                        .toHomeMovieItem(group = HomeMovieGroup.ON_THE_AIR_TV)
                }
                val topRatedTvs = async {
                    getTvsUseCase[TOP_RATED_TVS_KEY]!!(language)
                        .toHomeMovieItem(group = HomeMovieGroup.TOP_RATED_TV)
                }

                jobs.add(todayRecommendationMovie)
                jobs.add(topRatedMovies)
                jobs.add(nowPlayingMovies)
                jobs.add(airingTodayTvs)
                jobs.add(onTheAirTvs)
                jobs.add(topRatedTvs)

                val series = jobs.awaitAll().toImmutableList()

                _uiState.update { HomeUiState(HomeDisplayState.Contents(movies = series)) }
            },
            error = {
                val errorMessage = it.message ?: ""
                _uiState.update { HomeUiState(HomeDisplayState.Error(errorCode = "300", message = errorMessage)) }
            }
        )
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.GetTopRatedMovies -> {

            }
        }
    }

    private suspend fun Flow<PagingData<out Series>>.toHomeMovieItem(group: HomeMovieGroup): HomeMovieItem {
        return HomeMovieItem(
            group = group,
            series = map { pagingData ->
                val seenIds = mutableSetOf<String>()
                pagingData.filter { seenIds.add(it.id) }.map { it as Series }
            }
                .catch { }
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope)
        )
    }
}


