package com.dhkim.moviepick

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dhkim.common.Genre
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.Movie
import com.dhkim.domain.tv.model.Tv
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview(@PreviewParameter(SearchScreenPreviewParameter::class) uiState: SearchUiState) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchScreen(
                uiState,
                movieListState = rememberLazyListState(),
                tvListState = rememberLazyListState(),
                onAction = {},
                navigateToSeriesDetail = { _, _ -> },
                onBack = {}
            )
        }
    }
}

class SearchScreenPreviewParameter: PreviewParameterProvider<SearchUiState> {

    val movies = mutableListOf<Movie>().apply {
        add(
            Movie(
                id = "recommendationId",
                title = "recommendation title",
                adult = false,
                overview = "overview",
                genre = listOf(Genre.SCIENCE_FICTION.genre, Genre.ACTION.genre, Genre.DRAMA.genre),
                imageUrl = "imageUrl",
                releasedDate = "2025-03-13",
                voteAverage = 5.5,
                popularity = 45.38,
            )
        )
    }.toImmutableList()

    val tvs: ImmutableList<Tv> = mutableListOf<Tv>().apply {
        repeat(50) {
            add(
                Tv(
                    id = "topRatedId$it",
                    title = "top rated title$it",
                    adult = false,
                    overview = "overview $it",
                    genre = listOf(Genre.ACTION.genre, Genre.DRAMA.genre),
                    imageUrl = "imageUrl$it",
                    voteAverage = 5.5 + it.toDouble(),
                    popularity = 45.38 + it.toDouble(),
                    country = "미국",
                    firstAirDate = "2013-12-24"
                )
            )
        }
    }.toImmutableList()

    private val uiState = SearchUiState()

    override val values: Sequence<SearchUiState>
        get() = sequenceOf(
            uiState,
            uiState.copy(contentState = SearchContentState.Content(movies = movies, tvs= tvs)),
            uiState.copy(isLoading = true, contentState = SearchContentState.Content(movies = movies, tvs= tvs)),
            uiState.copy(query = "Nothing", contentState = SearchContentState.Empty),
            uiState.copy(query = "Error", contentState = SearchContentState.Error(message = "오류가 발생하였습니다."))
        )
}