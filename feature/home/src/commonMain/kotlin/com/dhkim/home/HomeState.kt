package com.dhkim.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.paging.compose.collectAsLazyPagingItems
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.Black80
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import io.ktor.http.Url
import kotlinx.collections.immutable.ImmutableList

@Stable
class HomeState(
    private val series: ImmutableList<HomeItem>,
    val listState: LazyListState
) {
    val isNotRecommendationSeriesShowing by derivedStateOf {
        listState.firstVisibleItemIndex >= 3
    }

    val showCategory by derivedStateOf {
        listState.firstVisibleItemIndex >= 2
    }

    var backgroundColor by mutableStateOf(Black50)
        private set

    var onBackgroundColor by mutableStateOf(Color.LightGray)
        private set

    var backgroundColors by mutableStateOf(listOf(Black50, Black80))
        private set

    fun updateBackgroundColor(color: Color) {
        this.backgroundColor = color
    }

    fun updateOnBackgroundColor(color: Color) {
        this.onBackgroundColor = color
    }

    companion object {

        fun Saver(
            series: ImmutableList<HomeItem>,
            listState: LazyListState,
        ): Saver<HomeState, *> = Saver(
            save = { listOf(it.showCategory) },
            restore = {
                HomeState(
                    series = series,
                    listState = listState,
                )
            }
        )
    }
}

@Composable
fun rememberHomeState(
    homeMovieItems: ImmutableList<HomeItem>,
    listState: LazyListState = rememberLazyListState()
): HomeState {
    val state = rememberSaveable(homeMovieItems, listState, saver = HomeState.Saver(series = homeMovieItems, listState = listState)) {
        HomeState(series = homeMovieItems, listState = listState)
    }
    val recommendationSeries = homeMovieItems
        .firstOrNull { it.group == HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE }
        ?.run { (this as HomeItem.HomeMovieItem).series }
        ?.collectAsLazyPagingItems()
    val recommendationSeriesPosterUrl = if (!recommendationSeries?.itemSnapshotList.isNullOrEmpty()) {
        recommendationSeries?.itemSnapshotList?.firstOrNull()?.imageUrl ?: ""
    } else {
        ""
    }
    val networkLoader = rememberNetworkLoader()
    val dominantColorState = rememberDominantColorState(loader = networkLoader, defaultColor = MaterialTheme.colorScheme.background)
    val vibrantColor = dominantColorState.result
        ?.paletteOrNull
        ?.getVibrantColor(Black50.toArgb())
        ?: MaterialTheme.colorScheme.background.toArgb()

    LaunchedEffect(recommendationSeriesPosterUrl, listState) {
        dominantColorState.updateFrom(Url(recommendationSeriesPosterUrl))
        state.updateBackgroundColor(dominantColorState.color.copy(alpha = 0.8f))
        state.updateOnBackgroundColor(dominantColorState.onColor.copy(alpha = 0.8f))
    }

    return state
}