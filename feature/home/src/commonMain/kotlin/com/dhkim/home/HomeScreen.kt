package com.dhkim.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.designsystem.Black
import com.dhkim.core.designsystem.DarkGray60
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.MoviePickButton
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.RecommendationSeriesScope
import com.dhkim.core.ui.Resources
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    uiState: HomeUiState
) {
    when (uiState.displayState) {
        HomeDisplayState.Loading -> {

        }

        is HomeDisplayState.Contents -> {
            val movies = uiState.displayState.movies
            ContentsScreen(movies)
        }

        is HomeDisplayState.Error -> {

        }
    }
}

@Composable
fun ContentsScreen(homeMovieItems: ImmutableList<HomeMovieItem>) {
    LazyColumn {
        items(items = homeMovieItems, key = { item -> item.group }) {
            val movies = it.series.collectAsLazyPagingItems()
            when (it.group) {
                HomeMovieGroup.TODAY_RECOMMENDATION_MOVIE -> {
                    if (movies.itemCount > 0) {
                        RecommendationSeries(series = movies[0] as Movie) {
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
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                )
                Text(
                    text = "찜",
                    style = MoviePickTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Composable
fun SeriesList(title: String, series: LazyPagingItems<Series>, seriesItem: @Composable (Series) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MoviePickTheme.typography.titleMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
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
            .size(250.dp),
        imageModel = { movie.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
    )
}

@Composable
fun TvItem(tv: Tv) {
    CoilImage(
        modifier = Modifier
            .size(250.dp),
        imageModel = { tv.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.TvPosterSample)
    )
}