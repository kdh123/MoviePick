package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
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
import kotlinx.coroutines.flow.onStart
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
                val jobs = mutableListOf<Deferred<SeriesItem.MovieSeriesItem>>()
                val todayRecommendationMovie = async {
                    getMoviesUseCase[TODAY_RECOMMENDATION_MOVIE_KEY]!!(language, region)
                        .toMovieItem(group = Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE, viewModelScope)
                }
                val todayTop10Movies = async {
                    getMoviesUseCase[TODAY_TOP_10_MOVIES_KEY]!!(language, region)
                        .toMovieItem(group = Group.HomeGroup.TODAY_TOP_10_MOVIES, viewModelScope)
                }
                val topRatedMovies = async {
                    getMoviesUseCase[TOP_RATED_MOVIES_KEY]!!(language, region)
                        .toMovieItem(group = Group.HomeGroup.TOP_RATED_MOVIE, viewModelScope)
                }
                val nowPlayingMovies = async {
                    getMoviesUseCase[NOW_PLAYING_MOVIES_KEY]!!(language, region)
                        .toMovieItem(group = Group.HomeGroup.NOW_PLAYING_MOVIE, viewModelScope)
                }
                val airingTodayTvs = async {
                    getTvsUseCase[AIRING_TODAY_TVS_KEY]!!(language)
                        .toMovieItem(group = Group.HomeGroup.AIRING_TODAY_TV, viewModelScope)
                }
                val onTheAirTvs = async {
                    getTvsUseCase[ON_THE_AIR_TVS_KEY]!!(language)
                        .toMovieItem(group = Group.HomeGroup.ON_THE_AIR_TV, viewModelScope)
                }
                val topRatedTvs = async {
                    getTvsUseCase[TOP_RATED_TVS_KEY]!!(language)
                        .toMovieItem(group = Group.HomeGroup.TOP_RATED_TV, viewModelScope)
                }

                jobs.add(todayRecommendationMovie)
                jobs.add(todayTop10Movies)
                jobs.add(topRatedMovies)
                jobs.add(nowPlayingMovies)
                jobs.add(airingTodayTvs)
                jobs.add(onTheAirTvs)
                jobs.add(topRatedTvs)

                val series = listOf(
                    SeriesItem.AppBar(group = Group.HomeGroup.APP_BAR),
                    SeriesItem.Category(group = Group.HomeGroup.CATEGORY)
                ) + jobs.awaitAll()
                    .map { SeriesItem.MovieSeriesItem(it.group, it.series) }

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


