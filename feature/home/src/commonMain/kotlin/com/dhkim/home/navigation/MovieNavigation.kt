package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.SeriesType
import com.dhkim.home.movie.MovieScreen
import com.dhkim.home.movie.MovieViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val MOVIE_ROUTE = "movie_route"

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.movie(
    sharedTransitionScope: SharedTransitionScope,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onBack: () -> Unit
) {
    composable(MOVIE_ROUTE) {
        val viewModel = koinViewModel<MovieViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

        MovieScreen(
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