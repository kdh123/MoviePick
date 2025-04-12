package com.dhkim.upcoming.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.upcoming.UpcomingScreen
import com.dhkim.upcoming.UpcomingUiState
import com.dhkim.upcoming.UpcomingViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val UPCOMING_ROUTE = "upcoming_route"

fun NavController.navigateToUpcoming() = navigate(UPCOMING_ROUTE)

@KoinExperimentalAPI
fun NavGraphBuilder.upcoming(
    navigateToDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    composable(UPCOMING_ROUTE) {
        val viewModel = koinViewModel<UpcomingViewModel>()
        val uiState: UpcomingUiState by viewModel.uiState.collectAsStateWithLifecycle()
        UpcomingScreen(
            uiState = uiState,
            navigateToDetail = navigateToDetail
        )
    }
}