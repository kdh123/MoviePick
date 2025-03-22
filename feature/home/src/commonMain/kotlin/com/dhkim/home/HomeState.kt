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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.paging.compose.collectAsLazyPagingItems
import com.dhkim.core.designsystem.Black50
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import io.ktor.http.Url
import kotlinx.collections.immutable.ImmutableList

@Stable
class HomeState(
    private val series: ImmutableList<SeriesItem>,
    val listState: LazyListState
) {
    val showCategory by derivedStateOf {
        listState.firstVisibleItemIndex >= 2
    }

    var backgroundColor by mutableStateOf(Black50)
        private set

    var onBackgroundColor by mutableStateOf(Color.LightGray)
        private set

    var showTab by mutableStateOf(false)

    fun updateOnBackgroundColor(color: Color) {
        this.onBackgroundColor = color
    }

    var height by mutableStateOf(0)

    fun updateShowTab(show: Boolean) {
        showTab = show
    }

    fun updateHeight(height: Int) {
        this.height = height
    }

    fun updateBackgroundColor(backgroundColor: Color) {
        if (this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor
        }
    }

    companion object {

        fun Saver(
            series: ImmutableList<SeriesItem>,
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
    seriesItems: ImmutableList<SeriesItem>,
    mainRecommendationMovieGroup: Group,
    listState: LazyListState = rememberLazyListState()
): HomeState {
    val state = rememberSaveable(seriesItems, listState, saver = HomeState.Saver(series = seriesItems, listState = listState)) {
        HomeState(series = seriesItems, listState = listState)
    }
    val recommendationSeries = seriesItems
        .firstOrNull { it.group == mainRecommendationMovieGroup }
        ?.run { (this as SeriesItem.MovieSeriesItem).series }
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

    LaunchedEffect(recommendationSeriesPosterUrl) {
        dominantColorState.updateFrom(Url(recommendationSeriesPosterUrl))
        state.updateBackgroundColor(dominantColorState.color)
        state.updateOnBackgroundColor(dominantColorState.onColor)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }.collect { offset ->
            state.updateShowTab(show = listState.firstVisibleItemIndex >= 1)
            if (listState.firstVisibleItemIndex == 2) {
                val alpha = ((state.height.toFloat() - offset) / state.height).coerceIn(0f, 1f)
                state.updateBackgroundColor(backgroundColor = state.backgroundColor.copy(alpha = alpha))
            }
        }
    }

    return state
}