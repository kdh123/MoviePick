package com.dhkim.moviepick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.domain.movie.usecase.SearchMovieUseCase
import com.dhkim.domain.tv.usecase.SearchTvUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val searchMovieUseCase: SearchMovieUseCase,
    private val searchTvUseCase: SearchTvUseCase
) : ViewModel() {

    private val loadingFlow = MutableStateFlow(false)
    private val searchQueryFlow = MutableStateFlow("")
    val uiState: StateFlow<SearchUiState> = searchQueryFlow
        .debounce(1_000)
        .flatMapConcat { query ->
            flow {
                if (query.isEmpty()) {
                    emit(SearchUiState(isLoading = false, query = query, contentState = SearchContentState.Idle))
                } else {
                    val movies = searchMovieUseCase(query).first().toImmutableList()
                    val tvs = searchTvUseCase(query).first().toImmutableList()
                    val contentState = if (movies.isEmpty() && tvs.isEmpty()) {
                        SearchContentState.Empty
                    } else {
                        SearchContentState.Content(movies, tvs)
                    }
                    emit(SearchUiState(isLoading = false, query = query, contentState = contentState))
                }
            }.catch {
                emit(SearchUiState(isLoading = false, query = query, contentState = SearchContentState.Error(message = "오류가 발생했습니다.")))
            }.onCompletion {
                loadingFlow.update { false }
            }
        }.combine(loadingFlow) { uiState, isLoading ->
            uiState.copy(isLoading = isLoading)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState()
        )

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.TypeSearchQuery -> {
                typeSearchQuery(action.query)
            }
        }
    }

    private fun typeSearchQuery(query: String) {
        loadingFlow.update { true }
        searchQueryFlow.update { query }
    }
}