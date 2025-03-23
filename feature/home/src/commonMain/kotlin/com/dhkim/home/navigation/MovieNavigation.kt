package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.home.movie.MovieScreen
import com.dhkim.home.movie.MovieViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val MOVIE_ROUTE = "movie_route"

fun NavController.navigateToMovie() = navigate(MOVIE_ROUTE)

@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.movie(
    sharedTransitionScope: SharedTransitionScope,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    composable(MOVIE_ROUTE) {
        val viewModel = koinViewModel<MovieViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        MovieScreen(
            uiState = uiState,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            onAction = viewModel::onAction,
            navigateToVideo = navigateToVideo,
            onBack = onBack
        )
    }
}