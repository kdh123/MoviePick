package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.home.tv.TvScreen
import com.dhkim.home.tv.TvViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val TV_ROUTE = "tv_route"

fun NavController.navigateToTv() = navigate(TV_ROUTE)

@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.tv(
    sharedTransitionScope: SharedTransitionScope,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    composable(TV_ROUTE) {
        val viewModel = koinViewModel<TvViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TvScreen(
            uiState = uiState,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            onAction = viewModel::onAction,
            navigateToVideo = navigateToVideo,
            onBack = onBack
        )
    }
}