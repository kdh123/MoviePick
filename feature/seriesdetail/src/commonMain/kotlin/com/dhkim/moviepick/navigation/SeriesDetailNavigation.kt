package com.dhkim.moviepick.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.moviepick.SeriesDetailScreen
import com.dhkim.moviepick.SeriesDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

const val SERIES_DETAIL_ROUTE = "series_detail"

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.seriesDetail(
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    composable(
        "$SERIES_DETAIL_ROUTE/{series}/{seriesId}",
    ) {
        val series = it.arguments?.getString("series")!!
        val seriesId = it.arguments?.getString("seriesId")!!
        val parametersHolder = { parametersOf(series, seriesId) }
        val viewModel = koinViewModel<SeriesDetailViewModel>(parameters = parametersHolder)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

        SeriesDetailScreen(
            uiState = uiState,
            bookmarks = bookmarks,
            onAction = viewModel::onAction,
            navigateToVideo = navigateToVideo,
            onBack = onBack
        )
    }
}