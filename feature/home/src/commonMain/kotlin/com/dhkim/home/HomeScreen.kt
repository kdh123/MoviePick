package com.dhkim.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.common.Series
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import moviepick.feature.home.generated.resources.Res
import moviepick.feature.home.generated.resources.ic_smile
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
fun TvItem(tv: Tv) {
    CoilImage(
        modifier = Modifier
            .size(250.dp),
        imageModel = { tv.imageUrl },
        failure = {
            Image(
                painter = painterResource(Res.drawable.ic_smile),
                contentDescription = null
            )
        },
        previewPlaceholder = painterResource(Res.drawable.ic_smile)
    )
}

@Composable
fun MovieItem(movie: Movie) {
    CoilImage(
        modifier = Modifier
            .size(250.dp),
        imageModel = { movie.imageUrl },
        failure = {
            Image(
                painter = painterResource(Res.drawable.ic_smile),
                contentDescription = null
            )
        },
        previewPlaceholder = painterResource(Res.drawable.ic_smile)
    )
}
