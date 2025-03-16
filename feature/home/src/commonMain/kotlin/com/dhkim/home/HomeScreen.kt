package com.dhkim.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.designsystem.Black
import com.dhkim.core.designsystem.Black00
import com.dhkim.core.designsystem.Black10
import com.dhkim.core.designsystem.Black80
import com.dhkim.core.designsystem.DarkGray60
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.MoviePickButton
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.RecommendationSeriesScope
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import com.skydoves.landscapist.coil3.CoilImage
import io.ktor.http.Url
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    uiState: HomeUiState
) {
    var showCategory by rememberSaveable { mutableStateOf(true) }
    val networkLoader = rememberNetworkLoader()
    val dominantColorState = rememberDominantColorState(loader = networkLoader, defaultColor = MaterialTheme.colorScheme.background)
    var recommendationSeriesPosterUrl by rememberSaveable { mutableStateOf("") }
    var colors by remember { mutableStateOf(listOf(Black00, Black10)) }
    val lightVibrantColor = dominantColorState.result
        ?.paletteOrNull
        ?.getLightVibrantColor(Black80.toArgb())
        ?: MaterialTheme.colorScheme.background.toArgb()

    LaunchedEffect(recommendationSeriesPosterUrl) {
        dominantColorState.updateFrom(Url(recommendationSeriesPosterUrl))
        colors = listOf(
            dominantColorState.color.copy(alpha = 0.65f),
            Color(lightVibrantColor).copy(alpha = 0.65f),
        )
    }

    Column(
        modifier = Modifier
            .background(Brush.verticalGradient(colors))
            .fillMaxSize()
    ) {
        AppBar(showCategory)
        when (uiState.displayState) {
            HomeDisplayState.Loading -> {

            }

            is HomeDisplayState.Contents -> {
                val movies = uiState.displayState.movies
                ContentsScreen(
                    homeMovieItems = movies,
                    onUpdateRecommendationSeriesPosterUrl = { recommendationSeriesPosterUrl = it },
                    onUpdateShowCategory = { showCategory = it }
                )
            }

            is HomeDisplayState.Error -> {

            }
        }
    }
}

@Composable
fun AppBar(showCategory: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Search),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClick { }
            )
        }
        AnimatedVisibility(showCategory) {
            CategoryChips()
        }
    }
}

@Composable
fun CategoryChips() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Chip(
            borderColor = Color.LightGray,
            onClick = {}
        ) {
            Text(
                text = "시리즈",
                color = Color.LightGray,
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        }
        Chip(
            borderColor = Color.LightGray,
            onClick = {}
        ) {
            Text(
                text = "영화",
                color = Color.LightGray,
                style = MoviePickTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ContentsScreen(
    homeMovieItems: ImmutableList<HomeMovieItem>,
    onUpdateShowCategory: (Boolean) -> Unit,
    onUpdateRecommendationSeriesPosterUrl: (String) -> Unit
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                when {
                    available.y >= 0 -> onUpdateShowCategory(true)
                    available.y < 0 -> onUpdateShowCategory(false)
                }
                return Offset.Zero
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
    ) {
        items(items = homeMovieItems, key = { item -> item.group }) {
            val movies = it.series.collectAsLazyPagingItems()
            when (it.group) {
                HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE -> {
                    if (movies.itemCount > 0) {
                        val series = movies[0] as Movie
                        onUpdateRecommendationSeriesPosterUrl(series.imageUrl)

                        RecommendationSeries(
                            series = series
                        ) {
                            Genre()
                            RecommendationButtons()
                        }
                    }
                }

                HomeMovieGroup.NOW_PLAYING_MOVIE_TOP_10 -> {
                    SeriesList(title = it.group.title, series = movies) { movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                HomeMovieGroup.TOP_RATED_MOVIE -> {
                    SeriesList(title = it.group.title, series = movies) { movie ->
                        MovieItem(movie = movie as Movie)
                    }
                }

                HomeMovieGroup.AIRING_TODAY_TV -> {
                    SeriesList(title = it.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                HomeMovieGroup.ON_THE_AIR_TV -> {
                    SeriesList(title = it.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }

                HomeMovieGroup.TOP_RATED_TV -> {
                    SeriesList(title = it.group.title, series = movies) { tv ->
                        TvItem(tv = tv as Tv)
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationSeriesScope.Genre() {
    val movie = series as Movie

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = movie.genre, key = { it }) {
            Text(
                text = it,
                style = MoviePickTheme.typography.labelMedium,
                color = White,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RecommendationSeriesScope.RecommendationButtons() {
    val movie = series as Movie

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        if (movie.video) {
            MoviePickButton(
                color = White,
                onClick = {},
                modifier = Modifier
                    .weight(1f)
            ) {
                Row {
                    Icon(
                        painter = painterResource(Resources.Icon.Play),
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "티저",
                        style = MoviePickTheme.typography.labelLarge,
                        color = Black,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        MoviePickButton(
            color = DarkGray60,
            onClick = {},
            modifier = Modifier
                .weight(1f)
        ) {
            Row {
                Icon(
                    painter = painterResource(Resources.Icon.Add),
                    tint = White,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                )
                Text(
                    text = "찜",
                    style = MoviePickTheme.typography.labelLarge,
                    color = White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Composable
fun SeriesList(title: String, series: LazyPagingItems<Series>, seriesItem: @Composable (Series) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MoviePickTheme.typography.titleMedium,
            modifier = Modifier
                .padding(8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                count = series.itemCount,
                key = series.itemKey(key = {
                    it.id
                }),
                contentType = series.itemContentType()
            ) { index ->
                val item = series[index]
                if (item != null) {
                    seriesItem(item)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    CoilImage(
        modifier = Modifier
            .clip(RoundedCornerShape(12f))
            .width(108.dp)
            .aspectRatio(7f / 10f),
        imageModel = { movie.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
    )
}

@Composable
fun TvItem(tv: Tv) {
    CoilImage(
        modifier = Modifier
            .clip(RoundedCornerShape(12f))
            .width(108.dp)
            .aspectRatio(7f / 10f),
        imageModel = { tv.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.TvPosterSample)
    )
}