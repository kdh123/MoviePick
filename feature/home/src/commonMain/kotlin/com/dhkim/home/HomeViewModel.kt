package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.handle
import com.dhkim.core.movie.data.di.NOW_PLAYING_MOVIES_KEY
import com.dhkim.core.movie.data.di.TOP_RATED_MOVIES_KEY
import com.dhkim.core.movie.domain.model.Movie
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getMoviesUseCase: Map<String, GetMoviesUseCase>
) : ViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(HomeUiState(HomeDisplayState.Loading))
    val uiState: StateFlow<HomeUiState> = _uiState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        HomeUiState(HomeDisplayState.Loading)
    )

    private fun init() {
        val language = Language.Korea.code
        val region = Region.Korea.code

        viewModelScope.handle(
            block = {
                val topRatedMovies = getMoviesUseCase[TOP_RATED_MOVIES_KEY]!!(language, region)
                    .toHomeMovieItem(group = HomeMovieGroup.TOP_RATED)
                val nowPlayingMovies = getMoviesUseCase[NOW_PLAYING_MOVIES_KEY]!!(language, region)
                    .toHomeMovieItem(group = HomeMovieGroup.NOW_PLAYING_TOP_10)
                val movies = persistentListOf(topRatedMovies, nowPlayingMovies)

                _uiState.update { HomeUiState(HomeDisplayState.Contents(movies = movies)) }
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

    private suspend fun Flow<PagingData<Movie>>.toHomeMovieItem(group: HomeMovieGroup): HomeMovieItem {
        val movies = catch { error ->
            _uiState.update { HomeUiState(HomeDisplayState.Error(errorCode = "300", message = error.message ?: "")) }
        }
            .cachedIn(viewModelScope)
            .first()
        return HomeMovieItem(group = group, movie = flowOf(movies).stateIn(viewModelScope))
    }
}


