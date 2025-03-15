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
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.movie.domain.model.Movie
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
        items(items = homeMovieItems, key = { it.group }) {
            val movies = it.movie.collectAsLazyPagingItems()
            when (it.group) {
                HomeMovieGroup.NOW_PLAYING_TOP_10 -> {
                    MovieList(title = it.group.title, movies = movies)
                }
                HomeMovieGroup.TOP_RATED -> {
                    MovieList(title = it.group.title, movies = movies)
                }
            }
        }
    }
}

@Composable
fun MovieList(title: String, movies: LazyPagingItems<Movie>) {
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
                count = movies.itemCount,
                key = movies.itemKey(key = {
                    it.id
                }),
                contentType = movies.itemContentType()
            ) { index ->
                val item = movies[index]
                if (item != null) {
                    MovieItem(movie = item)
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
        failure = {
            Image(
                painter = painterResource(Res.drawable.ic_smile),
                contentDescription = null
            )
        },
        previewPlaceholder = painterResource(Res.drawable.ic_smile)
    )
}
