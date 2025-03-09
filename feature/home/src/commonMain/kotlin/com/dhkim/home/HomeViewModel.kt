package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import com.dhim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhim.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class HomeViewModel : ViewModel(), KoinComponent {
    val getTopRatedMoviesUseCase: GetMoviesUseCase by inject<GetMoviesUseCase>(qualifier = named("topRated"))
    val uiState: StateFlow<HomeUiState> = getTopRatedMoviesUseCase()
        .map { it.toUiState() }
        .catch {
            val error = it.message
            emit(HomeUiState(HomeDisplayState.Error("500", "${it.message}")))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            HomeUiState(HomeDisplayState.Loading)
        )

    private suspend fun PagingData<Movie>.toUiState(): HomeUiState {
        return HomeUiState(displayState = HomeDisplayState.Contents(movies = flowOf(this).stateIn(viewModelScope)))
    }
}


