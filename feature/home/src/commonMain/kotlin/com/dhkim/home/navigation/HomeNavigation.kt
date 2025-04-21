package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.home.HomeScreen
import com.dhkim.home.HomeUiState
import com.dhkim.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val HOME_ROUTE = "home_route"

@ExperimentalSharedTransitionApi
@ExperimentalCoroutinesApi
@KoinExperimentalAPI
fun NavGraphBuilder.home(
    sharedTransitionScope: SharedTransitionScope,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToMovie: () -> Unit,
    navigateToTv: () -> Unit,
) {
    composable(HOME_ROUTE) {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState: HomeUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

        HomeScreen(
            uiState = uiState,
            bookmarks = bookmarks,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            onAction = viewModel::onAction,
            navigateToSeriesDetail = navigateToSeriesDetail,
            navigateToVideo = navigateToVideo,
            navigateToMovie = navigateToMovie,
            navigateToTv = navigateToTv,
        )
    }
}