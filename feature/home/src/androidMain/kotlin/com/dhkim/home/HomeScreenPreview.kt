package com.dhkim.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {

    val movies = mutableListOf<Movie>().apply {
        repeat(10) {
            add(
                Movie(
                    id = "id$it",
                    title = "title$it",
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    releasedDate = "2025-03-13",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble()
                )
            )
        }
    }

    val pagingData = MutableStateFlow(PagingData.from(movies)).asStateFlow()

   /* HomeScreen(
        uiState = HomeUiState(displayState = HomeDisplayState.Contents(pagingData))
    )*/
}