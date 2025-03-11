package com.dhkim.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.dhkim.core.movie.domain.model.Movie
import com.skydoves.landscapist.coil3.CoilImage
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
            val movies = uiState.displayState.movies.collectAsLazyPagingItems()
            ContentsScreen(movies)
        }

        is HomeDisplayState.Error -> {

        }
    }
}

@Composable
fun ContentsScreen(movies: LazyPagingItems<Movie>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
