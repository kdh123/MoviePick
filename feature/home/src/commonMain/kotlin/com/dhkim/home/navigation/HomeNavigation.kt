package com.dhkim.home.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.home.HomeScreen
import com.dhkim.home.HomeUiState
import com.dhkim.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome() = navigate(HOME_ROUTE)

@ExperimentalCoroutinesApi
@KoinExperimentalAPI
fun NavGraphBuilder.home(
    navigateToVideo: (String) -> Unit,
    navigateToMovie: () -> Unit
) {
    composable(HOME_ROUTE) {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState: HomeUiState by viewModel.uiState.collectAsStateWithLifecycle()
        HomeScreen(
            uiState = uiState,
            navigateToVideo = navigateToVideo,
            navigateToMovie = navigateToMovie
        )
    }
}