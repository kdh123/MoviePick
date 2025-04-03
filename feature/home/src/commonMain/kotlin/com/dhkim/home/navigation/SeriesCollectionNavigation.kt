package com.dhkim.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.common.Genre
import com.dhkim.home.series.SeriesCollectionScreen
import com.dhkim.home.series.SeriesCollectionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

const val SERIES_COLLECTION_ROUTE = "series_collection"

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
@KoinExperimentalAPI
fun NavGraphBuilder.seriesCollection(
    onBack: () -> Unit
) {
    composable(
        "$SERIES_COLLECTION_ROUTE/{series}/{genreId}/{region}",
    ) {
        val series = it.arguments?.getString("series")!!
        val genreId: Int? = it.arguments?.getString("genreId")?.run {
            if (this == "null") Genre.Unknown.id else toInt()
        }
        val region = it.arguments?.getString("region")?.run {
            if (this == "null") null else this
        }
        val parametersHolder = { parametersOf(series, genreId, region) }
        val viewModel = koinViewModel<SeriesCollectionViewModel>(parameters = parametersHolder)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SeriesCollectionScreen(
            uiState = uiState,
            onBack = onBack
        )
    }
}