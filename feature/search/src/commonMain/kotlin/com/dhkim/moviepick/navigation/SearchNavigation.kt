package com.dhkim.moviepick.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.moviepick.SearchScreen
import com.dhkim.moviepick.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val SEARCH_ROUTE = "search_route"

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.search(
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    onBack: () -> Unit
) {
    composable(SEARCH_ROUTE) {
        val viewModel = koinViewModel<SearchViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SearchScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
            onBack = onBack,
            navigateToSeriesDetail = navigateToSeriesDetail
        )
    }
}