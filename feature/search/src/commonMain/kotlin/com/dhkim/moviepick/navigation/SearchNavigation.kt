package com.dhkim.moviepick.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.moviepick.SearchScreen
import com.dhkim.moviepick.SearchSideEffect
import com.dhkim.moviepick.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val SEARCH_ROUTE = "search_route"

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.search(
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    composable(SEARCH_ROUTE) {
        val viewModel = koinViewModel<SearchViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val movieListState = rememberLazyListState()
        val tvListState = rememberLazyListState()

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest {
                when (it) {
                    SearchSideEffect.ScrollToFirstItem -> {
                        try {
                            movieListState.scrollToItem(0)
                            tvListState.scrollToItem(0)
                        } catch (_: Exception) {

                        }
                    }
                }
            }
        }

        SearchScreen(
            uiState = uiState,
            movieListState = movieListState,
            tvListState = tvListState,
            onAction = viewModel::onAction,
            navigateToSeriesDetail = navigateToSeriesDetail
        )
    }
}