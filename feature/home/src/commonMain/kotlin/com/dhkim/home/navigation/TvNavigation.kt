package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.home.tv.TvScreen
import com.dhkim.home.tv.TvViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val TV_ROUTE = "tv_route"

@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.tv(
    sharedTransitionScope: SharedTransitionScope,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onBack: () -> Unit
) {
    composable(TV_ROUTE) {
        val viewModel = koinViewModel<TvViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

        TvScreen(
            uiState = uiState,
            bookmarks = bookmarks,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            onAction = viewModel::onAction,
            navigateToSeriesDetail = navigateToSeriesDetail,
            navigateToVideo = navigateToVideo,
            navigateToSeriesCollection = navigateToSeriesCollection,
            onBack = onBack
        )
    }
}